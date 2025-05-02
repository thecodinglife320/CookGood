package com.example.cameraxapp

import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.cameraxapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

   private lateinit var viewBinding: ActivityMainBinding

   private var imageCapture: ImageCapture? = null

   private lateinit var cameraExecutor: ExecutorService

   private val activityResultLauncher =
      registerForActivityResult(
         ActivityResultContracts.RequestMultiplePermissions()
      ) { permissions ->
         // Handle Permission granted/rejected
         var permissionGranted = true

         permissions.entries.forEach {
            if (it.key in REQUIRED_PERMISSIONS && it.value == false)
               permissionGranted = false
         }

         if (!permissionGranted) {
            Toast.makeText(
               baseContext,
               "Permission request denied",
               Toast.LENGTH_SHORT
            ).show()
         } else {
            startCamera()
         }
      }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      viewBinding = ActivityMainBinding.inflate(layoutInflater)
      setContentView(viewBinding.root)

      //request camera permission
      if (allPermissionsGranted()) {
         startCamera()
      } else {
         requestPermissions()
      }

      viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }
      viewBinding.videoCaptureButton.setOnClickListener { captureVideo() }

      cameraExecutor = Executors.newSingleThreadExecutor()
   }

   private fun takePhoto() {

      // Get a stable reference of the modifiable image capture use case
      val imageCapture = imageCapture ?: return

      // Create time stamped name and MediaStore entry.
      val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
         .format(System.currentTimeMillis())

      val contentValues = ContentValues().apply {
         put(MediaStore.MediaColumns.DISPLAY_NAME, name)
         put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
         if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
         }
      }

      // Create output options object which contains file + metadata
      val outputOptions = ImageCapture.OutputFileOptions
         .Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
         )
         .build()

      // Set up image capture listener, which is triggered after photo has
      // been taken
      imageCapture.takePicture(
         outputOptions,
         ContextCompat.getMainExecutor(this),
         object : ImageCapture.OnImageSavedCallback {

            override fun onError(exc: ImageCaptureException) {
               Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
               val msg = "Photo capture succeeded: ${output.savedUri}"
               Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
               Log.d(TAG, msg)
            }
         }
      )

      imageCapture.takePicture(
         ContextCompat.getMainExecutor(this),
         object : ImageCapture.OnImageCapturedCallback() {
            @OptIn(ExperimentalGetImage::class)
            override fun onCaptureSuccess(imageProxy: ImageProxy) {
               val image = imageProxy.image
               image?.let {
                  val bitmap = image.toBitmap().rotateBitmap(imageProxy.imageInfo.rotationDegrees)
                  Log.d(TAG, bitmap.toString())
               }
               image?.close()
            }

            override fun onError(exception: ImageCaptureException) {
               Log.d(TAG, exception.message.toString())
            }
         }
      )
   }

   private fun captureVideo() {}

   private fun startCamera() {
      val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

      cameraProviderFuture.addListener({
         // Used to bind the lifecycle of cameras to the lifecycle owner
         val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

         // Preview use case
         val preview = Preview.Builder()
            .build()
            .also {
               it.surfaceProvider = viewBinding.viewFinder.surfaceProvider
            }

         //image capture use case
         imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY) // hoặc MAXIMIZE_QUALITY
            .setJpegQuality(IMAGE_QUALITY) // <-- Đặt chất lượng JPEG ở đây (ví dụ: 80)
            .build()

         // Select back camera as a default
         val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

         try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()

            // Bind use cases to camera
            cameraProvider.bindToLifecycle(
               this, cameraSelector, preview, imageCapture
            )

         } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
         }

      }, ContextCompat.getMainExecutor(this))
   }

   private fun requestPermissions() {
      activityResultLauncher.launch(REQUIRED_PERMISSIONS)
   }

   private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
      ContextCompat.checkSelfPermission(
         baseContext, it
      ) == PackageManager.PERMISSION_GRANTED
   }

   override fun onDestroy() {
      super.onDestroy()
      cameraExecutor.shutdown()
   }

   companion object {
      private const val TAG = "CameraXApp"
      private const val IMAGE_QUALITY = 80
      private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
      private val REQUIRED_PERMISSIONS =
         mutableListOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
         ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
               add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
         }.toTypedArray()
   }
}

fun Bitmap.rotateBitmap(rotationDegrees: Int): Bitmap {
   val matrix = Matrix().apply {
      postRotate(-rotationDegrees.toFloat())
      postScale(-1f, -1f)
   }

   return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Image.toBitmap(): Bitmap {
   val buffer = planes[0].buffer
   buffer.rewind()
   val bytes = ByteArray(buffer.capacity())
   buffer.get(bytes)
   return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}