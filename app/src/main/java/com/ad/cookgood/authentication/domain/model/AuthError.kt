package com.ad.cookgood.authentication.domain.model

import androidx.annotation.StringRes
import com.ad.cookgood.R

sealed class AuthError(@StringRes val messageRes: Int) {
   object InvalidCredential : AuthError(R.string.error_invalid_credential)
   object Unknown : AuthError(R.string.error_unknown)
   object UserCancelFlow : AuthError(R.string.error_user_cancel)
   object NoUserToLink : AuthError(R.string.error_no_user_to_link)
   object CredentialAlreadyInUse : AuthError(R.string.error_credential_in_use)
   object UserAlreadyLink : AuthError(R.string.error_user_already_link)
   object NetworkError : AuthError(R.string.network_error)
}