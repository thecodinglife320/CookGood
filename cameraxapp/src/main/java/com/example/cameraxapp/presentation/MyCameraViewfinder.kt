package com.example.cameraxapp.presentation

import androidx.camera.compose.CameraXViewfinder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CameraPreviewContent(
   viewModel: CameraPreviewViewModel,
   modifier: Modifier = Modifier,
   lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
   val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()
   val context = LocalContext.current

   LaunchedEffect(lifecycleOwner) {
      viewModel.bindToCamera(context.applicationContext, lifecycleOwner)
   }

   surfaceRequest?.let { request ->
      CameraXViewfinder(
         surfaceRequest = request,
         modifier = modifier
      )
   }
}