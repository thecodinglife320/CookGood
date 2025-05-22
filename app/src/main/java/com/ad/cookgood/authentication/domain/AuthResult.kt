package com.ad.cookgood.authentication.domain

sealed class AuthResult {
   object Success : AuthResult()
   data class Error(val reason: AuthError) : AuthResult()
}