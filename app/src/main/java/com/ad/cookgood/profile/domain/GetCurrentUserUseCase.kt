package com.ad.cookgood.profile.domain

import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
   private val repository: UserProfileRepository
) {
   operator fun invoke() = repository.getCurrentUser()
}