package com.ad.cookgood.authentication.data

import android.app.Activity
import androidx.core.net.toUri
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.ad.cookgood.authentication.domain.AuthError
import com.ad.cookgood.authentication.domain.AuthRepository
import com.ad.cookgood.authentication.domain.AuthResult
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class AuthRepositoryImpl @Inject constructor(
   @Named("GoogleClientId") private val clientId: String,
   private val firebaseAuth: FirebaseAuth
) : AuthRepository {

   override suspend fun signInWithGoogle(context: Activity) =
      try {
         val option = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(clientId)
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

         AuthResult.Success
      } catch (_: FirebaseAuthInvalidCredentialsException) {
         AuthResult.Error(AuthError.InvalidCredential)
      } catch (_: GetCredentialCancellationException) {
         AuthResult.Error(AuthError.UserCancelFlow)
      } catch (_: Exception) {
         AuthResult.Error(AuthError.Unknown)
      }

   override suspend fun signOut() =
      firebaseAuth.signOut()

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
