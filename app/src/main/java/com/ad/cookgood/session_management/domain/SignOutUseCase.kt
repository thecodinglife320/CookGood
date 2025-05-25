package com.ad.cookgood.session_management.domain

import javax.inject.Inject

class SignOutUseCase @Inject constructor(
   private val repository: SessionManagementRepository
) {
   operator fun invoke() = repository.signOut()
}