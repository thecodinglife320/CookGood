package com.ad.cookgood.authentication.presentation

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.R
import com.ad.cookgood.authentication.domain.model.AuthError
import com.ad.cookgood.authentication.domain.model.AuthResult
import com.ad.cookgood.authentication.domain.usecase.SignInAnonymousUseCase
import com.ad.cookgood.authentication.domain.usecase.SignInWithGoogleUseCase
import com.ad.cookgood.profile.domain.GetCurrentUserUseCase
import com.ad.cookgood.shared.SnackBarUiState
import com.ad.cookgood.util.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
   private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
   private val signInAnonymousUseCase: SignInAnonymousUseCase,
   private val getCurrentUserUseCase: GetCurrentUserUseCase,
   private val application: Application
) : ViewModel() {

   init {
      viewModelScope.launch {
         getCurrentUserUseCase().first()?.let {
            handleAuthResult(AuthResult.Success)
         }
      }
   }

   private val _snackBarUiState = MutableStateFlow(SnackBarUiState())

   private val _isLoadingGoogle = MutableStateFlow(false)
   private val _isLoadingAnonymous = MutableStateFlow(false)

   val isLoadingGoogle: StateFlow<Boolean> = _isLoadingGoogle
   val isLoadingAnonymous: StateFlow<Boolean> = _isLoadingAnonymous

   //expose state
   val snackBarUiState: StateFlow<SnackBarUiState> = _snackBarUiState

   private fun handleAuthResult(authResult: AuthResult) {
      _snackBarUiState.value = _snackBarUiState.value.copy(
         showSnackBar = true
      )
      when (authResult) {
         is AuthResult.Error -> {
            _snackBarUiState.value = _snackBarUiState.value.copy(
               message = application.getString(authResult.reason.messageRes),
               isError = true
            )
         }

         AuthResult.Success -> {
            _snackBarUiState.value = _snackBarUiState.value.copy(
               message = application.getString(R.string.message_success),
               isError = false
            )
         }

         is AuthResult.LinkSuccess -> ""
      }
      _isLoadingGoogle.value = false
      _isLoadingAnonymous.value = false
   }

   fun onDismissSnackBar() {
      _snackBarUiState.value = _snackBarUiState.value.copy(
         showSnackBar = false
      )
   }

   fun signInWithGoogle(context: Activity) {
      if (isNetworkAvailable(context)) {
         viewModelScope.launch {
            _isLoadingGoogle.value = true
            handleAuthResult(signInWithGoogleUseCase(context))
         }
      } else handleAuthResult(AuthResult.Error(AuthError.NetworkError))
   }

   fun signInAnonymous() {
      if (isNetworkAvailable(application)) {
         viewModelScope.launch {
            _isLoadingAnonymous.value = true
            handleAuthResult(signInAnonymousUseCase())
         }
      } else handleAuthResult(AuthResult.Error(AuthError.NetworkError))
   }

   private companion object {
      private const val TIMEOUT_MILLIS = 5_000L
      const val TAG = "AuthViewModel"
   }
}