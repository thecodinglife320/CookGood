package com.ad.cookgood.authentication.data

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.ad.cookgood.BuildConfig
import com.ad.cookgood.authentication.domain.AuthRepository
import com.ad.cookgood.authentication.domain.model.AuthError
import com.ad.cookgood.authentication.domain.model.AuthResult
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class AuthRepositoryImpl @Inject constructor(
   private val firebaseAuth: FirebaseAuth
) : AuthRepository {

   private var name: String? = ""
   private var url: Uri? = null

   override suspend fun signInWithGoogle(context: Activity) =
      try {
         val authCredential = getGoogleAuthCredential(context)
         firebaseAuth.signInWithCredential(authCredential).await()
         AuthResult.Success
      } catch (_: FirebaseAuthInvalidCredentialsException) {
         AuthResult.Error(AuthError.InvalidCredential)
      } catch (_: GetCredentialCancellationException) {
         AuthResult.Error(AuthError.UserCancelFlow)
      } catch (e: Exception) {
         Log.e(TAG, "Unknown error during Google sign-in", e)
         AuthResult.Error(AuthError.Unknown)
      }

   override suspend fun signInAnonymous() =
      run {
         firebaseAuth.signInAnonymously().await()
         AuthResult.Success
      }

   override suspend fun linkAnonymous(context: Activity) =
      try {
         val currentUser = firebaseAuth.currentUser
         val authCredential = getGoogleAuthCredential(context)
         currentUser!!.linkWithCredential(authCredential).await()
         AuthResult.LinkSuccess(name, url)
      } catch (_: FirebaseAuthInvalidCredentialsException) {
         AuthResult.Error(AuthError.InvalidCredential)
      } catch (_: GetCredentialCancellationException) {
         AuthResult.Error(AuthError.UserCancelFlow)
      } catch (_: FirebaseAuthUserCollisionException) {
         AuthResult.Error(AuthError.CredentialAlreadyInUse)
      } catch (e: Exception) {
         Log.e(TAG, "Unknown error during anonymous linking", e)
         AuthResult.Error(AuthError.Unknown)
      }

   private suspend fun getGoogleAuthCredential(context: Activity): AuthCredential {
      val option = GetGoogleIdOption.Builder()
         .setFilterByAuthorizedAccounts(false)
         .setServerClientId(BuildConfig.GOOGLECLIENTID)
         .setAutoSelectEnabled(true) // Consider if this is always the desired behavior
         .build()

      val request = GetCredentialRequest.Builder()
         .addCredentialOption(option)
         .build()

      val response = CredentialManager.create(context).getCredential(context, request)
      val tokenCredential = GoogleIdTokenCredential.createFrom(response.credential.data)
      name = tokenCredential.displayName
      url = tokenCredential.profilePictureUri
      return GoogleAuthProvider.getCredential(tokenCredential.idToken, null)
   }

   private companion object {
      const val TAG = "AuthRepositoryImpl"
   }
}
