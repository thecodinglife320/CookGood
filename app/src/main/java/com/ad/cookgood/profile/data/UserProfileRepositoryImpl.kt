package com.ad.cookgood.profile.data

import android.net.Uri
import com.ad.cookgood.profile.domain.UserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
   private val firebaseAuth: FirebaseAuth
) : UserProfileRepository {

   override fun getCurrentUser() = callbackFlow {
      val authStateListener = FirebaseAuth.AuthStateListener { auth ->
         trySend(auth.currentUser)
      }

      firebaseAuth.addAuthStateListener(authStateListener)

      awaitClose {
         firebaseAuth.removeAuthStateListener(authStateListener)
      }
   }

   override suspend fun updateUserProfile(name: String?, url: Uri?) {
      val profileUpdates = userProfileChangeRequest {
         displayName = name
         photoUri = url
      }
      firebaseAuth.currentUser!!.updateProfile(profileUpdates).await()
   }
}