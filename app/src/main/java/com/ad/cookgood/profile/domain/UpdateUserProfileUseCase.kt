package com.ad.cookgood.profile.domain

import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
   private val repository: UserProfileRepository
) {
   suspend operator fun invoke(name: String, url: String) {
      repository.updateUserProfile(name, url)
   }
}