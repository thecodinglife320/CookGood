package com.ad.cookgood.captureimage.domain

import javax.inject.Inject

class TakePhotoUseCase @Inject constructor(
   private val repository: CameraRepository
) {
   suspend operator fun invoke() =
      repository.takePhoto()
}

