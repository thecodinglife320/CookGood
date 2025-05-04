package com.example.cameraxapp.domain

import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.Flow

interface CameraRepository {
   fun startCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView)
   fun stopCamera()
   suspend fun takePhoto(): Flow<Uri?>
}