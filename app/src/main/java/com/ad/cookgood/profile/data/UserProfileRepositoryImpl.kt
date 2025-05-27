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
         // Mỗi khi trạng thái thay đổi, emit người dùng hiện tại
         trySend(auth.currentUser)
      }

      // Thêm listener vào FirebaseAuth
      firebaseAuth.addAuthStateListener(authStateListener)

      // Khi Flow không còn được thu thập (collect) nữa, loại bỏ listener
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