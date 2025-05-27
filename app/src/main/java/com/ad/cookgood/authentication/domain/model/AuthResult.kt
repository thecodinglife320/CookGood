package com.ad.cookgood.authentication.domain.model

import android.net.Uri

sealed class AuthResult {
   object Success : AuthResult()
   data class Error(val reason: AuthError) : AuthResult()
}

sealed class LinkResult {
   data class Success(val name: String?, val url: Uri?) : LinkResult()
   data class Error(val reason: AuthError) : LinkResult()
}