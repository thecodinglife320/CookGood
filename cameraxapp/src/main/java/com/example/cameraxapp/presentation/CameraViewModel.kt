package com.example.cameraxapp.presentation

import android.net.Uri
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cameraxapp.domain.CameraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
   private val repository: CameraRepository
) : ViewModel() {

   // StateFlow để giữ Uri của ảnh đã chụp
   private val _photoUri = MutableStateFlow<Uri?>(null)
   val photoUri: StateFlow<Uri?> = _photoUri.asStateFlow()

   fun startCamera(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
      repository.startCamera(lifecycleOwner, previewView)
   }

   fun stopCamera() = repository.stopCamera()

   fun takePhoto() {

      viewModelScope.launch {
         _photoUri.value = null // Xóa Uri cũ

         // Gọi Use Case để lấy Flow chụp ảnh
         repository.takePhoto()
            .onEach {
               Log.d(TAG, it.toString())
               _photoUri.value = it
            }
            .catch {
               Log.e(TAG, it.message, it)
            }
      }
   }

   private companion object {
      const val TAG = "CameraViewModel"
   }

}