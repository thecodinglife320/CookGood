package com.ad.cookgood.authentication.domain

sealed class AuthError {
   object InvalidCredential : AuthError()
   object Unknown : AuthError()
   object UserCancelFlow : AuthError()
}