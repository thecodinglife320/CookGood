package com.ad.cookgood.captureimage.domain

import androidx.camera.core.Preview
import javax.inject.Inject

class GetPreviewUseCase @Inject constructor(
   private val cameraRepository: CameraRepository
) {
   operator fun invoke(): Preview =
      cameraRepository.getPreviewUseCase()
}

