package com.ad.cookgood.captureimage.domain

import javax.inject.Inject

class StopCameraUseCase @Inject constructor(
   private val repository: CameraRepository
) {
   operator fun invoke() {
      repository.stopCamera()
   }
}