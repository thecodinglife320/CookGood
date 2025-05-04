package com.ad.cookgood.captureimage.domain

import android.net.Uri
import androidx.camera.core.Preview
import androidx.lifecycle.LifecycleOwner

interface CameraRepository {
   fun startCamera(lifecycleOwner: LifecycleOwner, surfaceProvider: Preview.SurfaceProvider)
   fun stopCamera()
   suspend fun takePhoto(): Uri?
}