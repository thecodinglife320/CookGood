package com.ad.cookgood.authentication.domain.usecase

import android.app.Activity
import com.ad.cookgood.authentication.domain.AuthRepository
import com.ad.cookgood.authentication.domain.model.AuthResult
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
   private val authRepository: AuthRepository
) {
   suspend operator fun invoke(context: Activity): AuthResult {
      return authRepository.signInWithGoogle(context)
   }
}