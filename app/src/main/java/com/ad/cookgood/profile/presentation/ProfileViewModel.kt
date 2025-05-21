package com.ad.cookgood.profile.presentation

import android.app.Activity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.login.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
   private val authRepository: AuthRepository
) : ViewModel() {

   private val _profileUiState: MutableStateFlow<ProfileUiState> =
      MutableStateFlow(ProfileUiState.Loading)
   val profileUiState: StateFlow<ProfileUiState> = _profileUiState

   private val _error: MutableState<String?> = mutableStateOf(null)
   val error: State<String?> = _error

   private val coroutineExceptionHandler =
      CoroutineExceptionHandler { _, ex ->
         ex.printStackTrace()
         _error.value = ex.localizedMessage
      }

   init {
      loadUserProfile()
   }

   fun signIn(context: Activity) {
      viewModelScope.launch(coroutineExceptionHandler) {
         _profileUiState.value = ProfileUiState.Loading
         authRepository.signInWithGoogle(context)
         loadUserProfile()
      }
   }

   fun signOut() {
      viewModelScope.launch(coroutineExceptionHandler) {
         authRepository.signOut()
         _profileUiState.value = ProfileUiState.Loading
         authRepository.createGuest()
         loadUserProfile()
      }
   }

   private fun loadUserProfile() {
      authRepository.getCurrentUser()?.let {
         _profileUiState.value = ProfileUiState.Success(
            userId = it.uid,
            isAnonymous = it.isAnonymous,
            name = it.displayName,
            url = it.photoUrl,
         )
      }
   }
}
