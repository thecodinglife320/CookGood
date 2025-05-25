package com.ad.cookgood.authentication.domain.usecase

import com.ad.cookgood.authentication.domain.AuthRepository
import javax.inject.Inject

class SignInAnonymousUseCase @Inject constructor(
   private val authRepository: AuthRepository
) {
   suspend operator fun invoke() = authRepository.signInAnonymous()
}