package com.example.cameraxapp.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus.Denied
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPreview(
   modifier: Modifier = Modifier,
   stopCamera: () -> Unit,
   startCamera: (LifecycleOwner, PreviewView) -> Unit,
   takePhoto: () -> Unit,
   uri: Uri?
) {

   val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

   if (cameraPermissionState.status is Denied) {
      if ((cameraPermissionState.status as Denied).shouldShowRationale) {
         val context = LocalContext.current
         Button(onClick = {
            val intent = Intent(
               Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
               Uri.fromParts("package", context.packageName, null)
            )
            context.startActivity(intent)
         }) { Text("di den cai dat") }
      } //di den cai dat
      else {
         Button(onClick = cameraPermissionState::launchPermissionRequest) { Text("cap quyen") }
      }//hoi cap quyen
   } else {
      val context = LocalContext.current
      val lifecycleOwner = LocalLifecycleOwner.current
      val previewView = remember { PreviewView(context) }

      DisposableEffect(lifecycleOwner) {
         startCamera(lifecycleOwner, previewView)

         onDispose {
            stopCamera()
         }
      }
      Column {
         Box(
            modifier,
            contentAlignment = Alignment.BottomCenter,
         ) {
            AndroidView(
               factory = { previewView }
            )
            OutlinedButton(
               onClick = takePhoto,
               modifier = Modifier
                  .size(60.dp)
                  .padding(8.dp),
               shape = CircleShape
            ) {}
         }

         uri?.let {
            AsyncImage(
               model = ImageRequest.Builder(context)
                  .data(uri)
                  .crossfade(true)
                  .build(),
               contentDescription = "Ảnh vừa chụp",
            )
         }
      }
   }
}