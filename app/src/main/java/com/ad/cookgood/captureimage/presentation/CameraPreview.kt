package com.ad.cookgood.captureimage.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus.Denied
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPreview(
   modifier: Modifier = Modifier,
   bindToCamera: (Context, LifecycleOwner) -> Unit,
   surfaceRequest: SurfaceRequest?,
   takePhoto: () -> Unit,
) {

   val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

   if (cameraPermissionState.status is Denied) {
      if ((cameraPermissionState.status as Denied).shouldShowRationale) {
         val context = LocalContext.current
         Button(onClick = {
            val intent = Intent(
               android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
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

      LaunchedEffect(lifecycleOwner) {
         bindToCamera(context, lifecycleOwner)
      }

      Box(
         modifier,
         contentAlignment = Alignment.BottomCenter,
      ) {

         surfaceRequest?.let { request ->
            CameraXViewfinder(
               surfaceRequest = request,
            )
         }

         OutlinedButton(
            onClick = takePhoto,
            modifier = Modifier
               .size(60.dp)
               .padding(8.dp),
            shape = CircleShape
         ) {}
      }
   }
}