package com.ad.cookgood.authentication.domain.model

import android.net.Uri

sealed class AuthResult {
   object Success : AuthResult()
   data class Error(val reason: AuthError) : AuthResult()
   data class LinkSuccess(val name: String?, val url: Uri?) : AuthResult()
}