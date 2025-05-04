package com.ad.cookgood.captureimage.data

import android.app.Application
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.ad.cookgood.captureimage.domain.CameraRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume

class CameraRepositoryImpl @Inject constructor(
   private val application: Application
) : CameraRepository {

   private lateinit var cameraProvider: ProcessCameraProvider
   private lateinit var previewUseCase: Preview
   private lateinit var imageCapture: ImageCapture

   override fun startCamera(
      lifecycleOwner: LifecycleOwner,
      surfaceProvider: Preview.SurfaceProvider
   ) {
      ProcessCameraProvider.getInstance(application).let {
         it.addListener(
            {
               cameraProvider = it.get()

               previewUseCase = Preview.Builder().build().also {
                  it.surfaceProvider = surfaceProvider
               }

               //image capture use case
               imageCapture = ImageCapture.Builder()
                  .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY) // hoặc MAXIMIZE_QUALITY
                  .setJpegQuality(IMAGE_QUALITY) // <-- Đặt chất lượng JPEG ở đây (ví dụ: 80)
                  .build()

               Log.d(TAG, imageCapture.toString())

               // Unbind use cases before rebinding
               cameraProvider.unbindAll()

               // Bind use cases to camera
               cameraProvider.bindToLifecycle(
                  lifecycleOwner,
                  CameraSelector.DEFAULT_BACK_CAMERA,
                  previewUseCase,
                  imageCapture
               )
               Log.d(TAG, "Camera bind successful")
            },
            ContextCompat.getMainExecutor(application)
         )
      }
   }

   override fun stopCamera() {
      cameraProvider.unbindAll()
      Log.d(TAG, "Camera unbound")
   }

   @kotlin.OptIn(ExperimentalCoroutinesApi::class)
   override suspend fun takePhoto() =
      withContext(Dispatchers.IO) {
         suspendCancellableCoroutine {
            val photoFile = File(application.filesDir, "${System.currentTimeMillis()}.jpg")
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
            imageCapture.takePicture(
               outputOptions,
               ContextCompat.getMainExecutor(application),
               object : ImageCapture.OnImageSavedCallback {
                  override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                     it.resume(outputFileResults.savedUri)
                  }

                  override fun onError(exception: ImageCaptureException) {
                     Log.e(TAG, exception.message, exception)
                  }
               }
            )
         }
      }


   private companion object {
      const val TAG = "CameraRepoImpl"
      const val IMAGE_QUALITY = 80
   }
}