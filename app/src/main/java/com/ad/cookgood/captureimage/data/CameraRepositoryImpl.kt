package com.ad.cookgood.captureimage.data

import android.app.Application
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.core.content.ContextCompat
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

   private val preview = Preview.Builder().build()

   private val capture = ImageCapture.Builder()
      .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
      .setJpegQuality(IMAGE_QUALITY)
      .build()

   override fun getPreviewUseCase() = preview

   override fun getCaptureUseCase() = capture

   @OptIn(ExperimentalCoroutinesApi::class)
   override suspend fun takePhoto() =
      withContext(Dispatchers.IO) {
         suspendCancellableCoroutine {
            val photoFile = File(application.filesDir, "${System.currentTimeMillis()}.jpg")
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
            capture.takePicture(
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