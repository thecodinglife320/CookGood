package com.example.cameraxapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.cameraxapp.ui.CookGoodTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
   @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent {
         CookGoodTheme {
//            val vm: CameraViewModel = hiltViewModel<CameraViewModel>()
//            val photoUri = vm.photoUri.collectAsState()
//            CameraPreview(
//               Modifier.height(300.dp),
//               stopCamera = vm::stopCamera,
//               startCamera = {a,b->
//                  vm.startCamera(a,b)
//               },
//               takePhoto = {vm.takePhoto()},
//               uri = photoUri.value
//            )
         }
      }
   }
}