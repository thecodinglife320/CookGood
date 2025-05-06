package com.ad.cookgood.captureimage.presentation

import android.net.Uri
import androidx.camera.core.Preview
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.captureimage.domain.StartCameraUseCase
import com.ad.cookgood.captureimage.domain.StopCameraUseCase
import com.ad.cookgood.captureimage.domain.TakePhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
   private val startCameraUseCase: StartCameraUseCase,
   private val stopCameraUseCase: StopCameraUseCase,
   private val takePhotoUseCase: TakePhotoUseCase,
) : ViewModel() {

   private val _uri: MutableState<Uri?> = mutableStateOf(
      null
   )

   val uri: State<Uri?> get() = _uri

   fun startCamera(lifecycleOwner: LifecycleOwner, surfaceProvider: Preview.SurfaceProvider) {
      startCameraUseCase(lifecycleOwner, surfaceProvider)
   }

   fun stopCamera() {
      stopCameraUseCase()
   }

   fun takePhoto() {
      viewModelScope.launch {
         _uri.value = takePhotoUseCase()
      }
   }
}

