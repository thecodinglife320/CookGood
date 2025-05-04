package com.ad.cookgood.captureimage.domain

import androidx.camera.core.Preview
import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject

class StartCameraUseCase @Inject constructor(
   private val repository: CameraRepository
) {
   operator fun invoke(lifecycleOwner: LifecycleOwner, surfaceProvider: Preview.SurfaceProvider) {
      repository.startCamera(lifecycleOwner, surfaceProvider)
   }
}