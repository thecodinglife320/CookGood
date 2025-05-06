package com.example.cameraxapp.domain

import android.net.Uri
import androidx.camera.view.LifecycleCameraController
import kotlinx.coroutines.flow.Flow

interface CameraRepository {
   suspend fun takePhoto(cameraController: LifecycleCameraController): Flow<Uri?>
}