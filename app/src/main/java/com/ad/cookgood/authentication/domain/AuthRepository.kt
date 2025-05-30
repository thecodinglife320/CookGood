package com.ad.cookgood.authentication.domain

import android.app.Activity
import com.ad.cookgood.authentication.domain.model.AuthResult

interface AuthRepository {
   suspend fun signInWithGoogle(context: Activity): AuthResult
   suspend fun signInAnonymous(): AuthResult
   suspend fun linkAnonymous(context: Activity): AuthResult
}

