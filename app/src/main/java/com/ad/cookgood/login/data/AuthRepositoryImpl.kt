package com.ad.cookgood.login.data

import android.app.Activity
import androidx.core.net.toUri
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.ad.cookgood.login.domain.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(

) : AuthRepository {

   private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

   override suspend fun signInWithGoogle(context: Activity) {
      val option = GetGoogleIdOption.Builder()
         .setFilterByAuthorizedAccounts(false)
         .setServerClientId("980760619934-onpras1tv3lcfkmirmfsk13oit2dref5.apps.googleusercontent.com")
         .setAutoSelectEnabled(false)
         .build()

      val request = GetCredentialRequest.Builder()
         .addCredentialOption(option)
         .build()

      val response = CredentialManager.create(context).getCredential(context, request)
      val oldUser = firebaseAuth.currentUser

      val tokenCredential = GoogleIdTokenCredential.createFrom(response.credential.data)
      val authCredential = GoogleAuthProvider.getCredential(tokenCredential.idToken, null)

      firebaseAuth.signInWithCredential(authCredential).await()
      oldUser?.delete()
   }

   override suspend fun signOut() {
      firebaseAuth.signOut()
   }

   override suspend fun createGuest() {
      firebaseAuth.signInAnonymously().await()
      val guest = firebaseAuth.currentUser ?: return
      updateUserProfile(
         name = "Guest ${guest.uid}",
         photo = "https://icons.iconarchive.com/icons/martin-berube/character/256/Devil-icon.png",
         user = guest
      )
   }

   private suspend fun updateUserProfile(name: String, photo: String, user: FirebaseUser) {
      val profileUpdates = userProfileChangeRequest {
         displayName = name
         photoUri = photo.toUri()
      }
      user.updateProfile(profileUpdates).await()
   }

   override fun getCurrentUser(): FirebaseUser? {
      return firebaseAuth.currentUser
   }
}
