package com.ad.cookgood.profile.domain

import android.net.Uri
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
   private val repository: UserProfileRepository
) {
   suspend operator fun invoke(name: String?, url: Uri?) {
      repository.updateUserProfile(name, url)
   }
}