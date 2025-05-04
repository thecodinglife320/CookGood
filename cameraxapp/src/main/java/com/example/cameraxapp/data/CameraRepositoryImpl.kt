package com.example.cameraxapp.data

import android.app.Application
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.cameraxapp.domain.CameraRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import javax.inject.Inject

class CameraRepositoryImpl @Inject constructor(
   private val application: Application
) : CameraRepository {

   private lateinit var cameraController: LifecycleCameraController

   override fun startCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
      cameraController = LifecycleCameraController(application).apply {
         bindToLifecycle(lifecycleOwner)
         previewView.controller = this
      }
   }

   override fun stopCamera() {
      cameraController.unbind()
   }

   override suspend fun takePhoto() = callbackFlow {

      awaitClose {
         Log.d(TAG, "Flow cancelled or closed. Unbinding CameraController.")
         // Quan trọng: Hủy ràng buộc controller khi Flow không còn được thu thập
         cameraController.unbind()
         Log.d(TAG, "CameraController unbound.")
      }

      val photoFile = File(application.filesDir, "${System.currentTimeMillis()}.jpg")
      val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
      cameraController.takePicture(
         outputOptions,
         ContextCompat.getMainExecutor(application),
         object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
               Log.d(TAG, "Photo capture succeeded: ${outputFileResults.savedUri}")
               trySend(outputFileResults.savedUri) // Phát ra Uri vào Flow (có thể null nếu savedUri null)
               close()
            }

            override fun onError(exception: ImageCaptureException) {
               Log.e(TAG, exception.message, exception)
               close(exception)
            }
         }
      )
   }

   private companion object {
      const val TAG = "CameraRepositoryImpl"
   }
}