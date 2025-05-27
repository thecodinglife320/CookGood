package com.ad.cookgood.authentication.domain.usecase

import android.app.Activity
import com.ad.cookgood.authentication.domain.AuthRepository
import javax.inject.Inject

class LinkAnonymousUseCase @Inject constructor(
   private val authRepository: AuthRepository
) {
   suspend operator fun invoke(context: Activity) = authRepository.linkAnonymous(context)
}