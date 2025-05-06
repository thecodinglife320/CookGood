package com.example.cameraxapp

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cameraxapp.presentation.CameraPreviewContent
import com.example.cameraxapp.presentation.CameraPreviewViewModel
import com.example.cameraxapp.ui.theme.CookGoodTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus.Denied
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
   @OptIn(ExperimentalPermissionsApi::class)
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent {
         CookGoodTheme {

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
               val vm = viewModel<CameraPreviewViewModel>()
               CameraPreviewContent(
                  viewModel = vm,
               )
            }
         }
      }
   }
}