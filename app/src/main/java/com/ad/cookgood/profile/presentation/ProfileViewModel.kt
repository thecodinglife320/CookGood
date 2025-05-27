package com.ad.cookgood.profile.presentation

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.R
import com.ad.cookgood.profile.domain.GetCurrentUserUseCase
import com.ad.cookgood.profile.domain.UpdateUserProfileUseCase
import com.ad.cookgood.util.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
   private val getCurrentUserUseCase: GetCurrentUserUseCase,
   private val updateUserProfileUseCase: UpdateUserProfileUseCase,
   private val application: Application
) : ViewModel() {

   private val _profileUiState: MutableStateFlow<ProfileUiState> =
      MutableStateFlow(ProfileUiState.Loading)
   val profileUiState: StateFlow<ProfileUiState> = _profileUiState

   private val _error: MutableState<String?> = mutableStateOf(null)
   val error: State<String?> = _error

   init {
      loadUserProfile()
   }

   private fun loadUserProfile() {
      if (isNetworkAvailable(application)) {
         viewModelScope.launch {
            getCurrentUserUseCase().collect {
               it?.let {
                  _profileUiState.value = ProfileUiState.Success(
                     userId = it.uid,
                     isAnonymous = it.isAnonymous,
                     name = it.displayName,
                     url = it.photoUrl,
                     email = it.email
                  )
               }
            }
         }
      } else _error.value = application.getString(R.string.network_error)
   }

   fun onNameChange(name: String) {
      _profileUiState.value = (_profileUiState.value as ProfileUiState.Success).copy(
         name = name
      )
   }

   fun updateUserProfile() {
      if (isNetworkAvailable(application)) {
         viewModelScope.launch {
            val name = (_profileUiState.value as ProfileUiState.Success).name!!
            val url = (_profileUiState.value as ProfileUiState.Success).url!!
            updateUserProfileUseCase(name, url)
         }
      } else _error.value = application.getString(R.string.network_error)
   }
}
