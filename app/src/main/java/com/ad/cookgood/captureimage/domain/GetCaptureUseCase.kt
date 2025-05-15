package com.ad.cookgood.captureimage.domain

import androidx.camera.core.ImageCapture
import javax.inject.Inject

class GetCaptureUseCase @Inject constructor(
   private val cameraRepository: CameraRepository
) {
   operator fun invoke(): ImageCapture =
      cameraRepository.getCaptureUseCase()
}