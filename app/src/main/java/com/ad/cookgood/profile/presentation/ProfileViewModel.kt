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
   val profileUiState: StateFlow<ProfileUiState> = _profileUiState

   private val _snackBarUiState = MutableStateFlow(SnackBarUiState())

   private var uploadedUserPhoto: String? = null

   //expose state
   val snackBarUiState: StateFlow<SnackBarUiState> = _snackBarUiState

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
      } else handleError()
   }

   fun onNameChange(name: String) {
      _profileUiState.value = (_profileUiState.value as ProfileUiState.Success).copy(
         name = name
      )
   }

   fun updateUserProfile() {
      if (isNetworkAvailable(application)) {
         viewModelScope.launch {
            uploadImage()
            val name = (_profileUiState.value as ProfileUiState.Success).name!!
            updateUserProfileUseCase(name, uploadedUserPhoto?.toUri())
            _snackBarUiState.value = _snackBarUiState.value.copy(
               message = application.getString(R.string.profile_updated),
               showSnackBar = true
            )
         }
      } else handleError()
   }

   fun handleError() {
      _snackBarUiState.value = _snackBarUiState.value.copy(
         message = application.getString(R.string.network_error),
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

   fun uploadImage() {
      viewModelScope.launch {
         val fileDetail = getFileDetailsFromUri(
            application,
            (_profileUiState.value as ProfileUiState.Success).uri!!
         )
         if (fileDetail != null) {
            val file = InputFile.fromBytes(
               fileDetail.bytes,
               fileDetail.filename!!,
               fileDetail.mimeType ?: "application/octet-stream"
            )
            val result = uploadImageUseCase(file, ID.unique(), BuildConfig.APPWRITE_BUCKET_ID)
            result.onSuccess {
               uploadedUserPhoto = getAppWriteFileViewUrl(
                  bucketId = BuildConfig.APPWRITE_BUCKET_ID,
                  fileId = it.id,
                  projectId = BuildConfig.APPWRITE_PROJECT_ID
               )
               Log.d(TAG, "uploadImage: $it")
            }.onFailure {
               Log.e(TAG, it.message.toString())
            }
         }
      }
   }

   companion object {
      const val TAG = "ProfileViewModel"
   }

}
