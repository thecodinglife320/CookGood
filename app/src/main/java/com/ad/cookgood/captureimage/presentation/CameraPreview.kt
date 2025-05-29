package com.ad.cookgood.captureimage.presentation

import android.content.Context
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun CameraPreview(
   modifier: Modifier = Modifier,
   bindToCamera: (Context, LifecycleOwner) -> Unit = { _, _ -> },
   surfaceRequest: SurfaceRequest? = null,
   takePhoto: () -> Unit = {},
) {

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