package com.ad.cookgood.profile.presentation

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.R
import com.ad.cookgood.authentication.domain.AuthError
import com.ad.cookgood.authentication.domain.AuthRepository
import com.ad.cookgood.authentication.domain.AuthResult
import com.ad.cookgood.util.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
   private val authRepository: AuthRepository,
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

   fun signIn(context: Activity) {
      if (isNetworkAvailable(context)) {
         viewModelScope.launch {
            _profileUiState.value = ProfileUiState.Loading

            when (val result = authRepository.signInWithGoogle(context)) {
               is AuthResult.Error -> handleAuthError(result.reason)
               AuthResult.Success -> loadUserProfile()
            }
         }
      } else _error.value = application.getString(R.string.network_error)
   }

   fun signOut(context: Context) {
      if (isNetworkAvailable(context)) {
         viewModelScope.launch {
            authRepository.signOut()
            _profileUiState.value = ProfileUiState.Loading
            authRepository.createGuest()
            loadUserProfile()
         }
      } else _error.value = application.getString(R.string.network_error)
   }

   private fun loadUserProfile() {
      if (isNetworkAvailable(application)) {
         authRepository.getCurrentUser()?.let {
            _profileUiState.value = ProfileUiState.Success(
               userId = it.uid,
               isAnonymous = it.isAnonymous,
               name = it.displayName,
               url = it.photoUrl,
            )
         }
      } else _error.value = application.getString(R.string.network_error)
   }

   private fun handleAuthError(error: AuthError) {
      _error.value = when (error) {
         is AuthError.InvalidCredential -> application.getString(R.string.credential_invalid)
         is AuthError.Unknown -> application.getString(R.string.unexpected_error)
         AuthError.UserCancelFlow -> null
      }
      loadUserProfile()
   }

   fun dismissError() {
      _error.value = null
   }

}
