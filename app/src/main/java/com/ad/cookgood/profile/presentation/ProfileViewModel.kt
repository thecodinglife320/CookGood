package com.ad.cookgood.profile.presentation

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.BuildConfig
import com.ad.cookgood.R
import com.ad.cookgood.profile.domain.GetCurrentUserUseCase
import com.ad.cookgood.profile.domain.UpdateUserProfileUseCase
import com.ad.cookgood.shared.SnackBarUiState
import com.ad.cookgood.uploadimage.domain.UploadImageUseCase
import com.ad.cookgood.util.getAppWriteFileViewUrl
import com.ad.cookgood.util.getFileDetailsFromUri
import com.ad.cookgood.util.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appwrite.ID
import io.appwrite.models.InputFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
   private val getCurrentUserUseCase: GetCurrentUserUseCase,
   private val updateUserProfileUseCase: UpdateUserProfileUseCase,
   private val uploadImageUseCase: UploadImageUseCase,
   private val application: Application,
) : ViewModel() {

   private val _profileUiState: MutableStateFlow<ProfileUiState> =
      MutableStateFlow(ProfileUiState.Loading)
   val profileUiState = _profileUiState.asStateFlow()

   private val _snackBarUiState = MutableStateFlow(SnackBarUiState())

   //expose state
   val snackBarUiState: StateFlow<SnackBarUiState> = _snackBarUiState

   private val _isUpdating = MutableStateFlow(false)
   val isUpdating: StateFlow<Boolean> = _isUpdating

   init {
      loadUserProfile()
   }

   private fun loadUserProfile() {
      if (isNetworkAvailable(application)) {
         viewModelScope.launch {
            getCurrentUserUseCase().collect {
               it?.let {
                  _profileUiState.value = ProfileUiState.Success(
                     name = it.displayName,
                     uri = it.photoUrl,
                     email = it.email
                  )
               }
            }
         }
      } else handleNetWorkError()
   }

   fun onNameChange(name: String) {
      _profileUiState.value = (_profileUiState.value as ProfileUiState.Success).copy(
         name = name
      )
   }

   fun updateUserProfile() {
      if (isNetworkAvailable(application)) {
         viewModelScope.launch {
            _isUpdating.value = true
            val fileDetail = getFileDetailsFromUri(
               application,
               (_profileUiState.value as ProfileUiState.Success).uri!!
            )
            fileDetail?.let {
               val file = InputFile.fromBytes(
                  fileDetail.bytes,
                  fileDetail.filename!!,
                  fileDetail.mimeType ?: "application/octet-stream"
               )
               uploadImageUseCase(file, ID.unique(), BuildConfig.APPWRITE_BUCKET_ID)
                  .onSuccess {
                     getAppWriteFileViewUrl(
                        bucketId = BuildConfig.APPWRITE_BUCKET_ID,
                        fileId = it.id,
                        projectId = BuildConfig.APPWRITE_PROJECT_ID
                     ).let {
                        updateUserProfileUseCase(
                           (_profileUiState.value as ProfileUiState.Success).name!!,
                           it.toUri()
                        )
                        _snackBarUiState.value = _snackBarUiState.value.copy(
                           message = application.getString(R.string.profile_updated),
                           isError = false,
                           showSnackBar = true
                        )
                        _isUpdating.value = false
                     }
                  }
                  .onFailure {
                     handleError(it)
                  }

            } ?: Log.d(TAG, "loi lay thong tin file")
         }
      } else handleNetWorkError()
   }

   fun handleNetWorkError() {
      _snackBarUiState.value = _snackBarUiState.value.copy(
         message = application.getString(R.string.network_error),
         isError = true,
         showSnackBar = true
      )
   }

   fun handleError(throwable: Throwable? = null) {
      Log.e(TAG, "handleError: ", throwable)
      _isUpdating.value = false
      _snackBarUiState.value = _snackBarUiState.value.copy(
         message = application.getString(R.string.profile_update_failed),
         isError = true,
         showSnackBar = true
      )
   }

   fun onDismissSnackBar() {
      _snackBarUiState.value = _snackBarUiState.value.copy(
         showSnackBar = false,
         actionLabel = null
      )
   }

   fun onImagePicked(uri: Uri) {
      _profileUiState.value = (_profileUiState.value as ProfileUiState.Success).copy(
         uri = uri
      )
   }

   companion object {
      const val TAG = "ProfileViewModel"
   }

}
