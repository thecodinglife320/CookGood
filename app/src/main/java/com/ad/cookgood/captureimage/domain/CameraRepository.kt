package com.ad.cookgood.captureimage.domain

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview

interface CameraRepository {
   fun getPreviewUseCase(): Preview
   fun getCaptureUseCase(): ImageCapture
   suspend fun takePhoto(): Uri?
}