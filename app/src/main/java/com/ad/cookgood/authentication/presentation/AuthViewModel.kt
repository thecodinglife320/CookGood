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
import com.ad.cookgood.util.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
   private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
   private val signInAnonymousUseCase: SignInAnonymousUseCase,
   getCurrentUserUseCase: GetCurrentUserUseCase,
   private val application: Application
) : ViewModel() {

   val currentUserId = getCurrentUserUseCase()
      .map {
         it?.uid.also {
            it?.let { handleAuthResult(AuthResult.Success) }
         }
      }
      .stateIn(
         scope = viewModelScope,
         started = SharingStarted.Lazily,
         initialValue = null
      )

   private val _messageUiState = MutableStateFlow(MessageUiState())

   //expose state
   val messageUiState: StateFlow<MessageUiState> = _messageUiState

   private fun handleAuthResult(authResult: AuthResult) {
      when (authResult) {
         is AuthResult.Error -> {
            _messageUiState.value =
               MessageUiState(application.getString(authResult.reason.messageRes))
         }

         AuthResult.Success -> {
            _messageUiState.value = MessageUiState(application.getString(R.string.message_success))
         }
      }
   }

   fun dismissMessage() {
      _messageUiState.value = MessageUiState()
   }

   fun signInWithGoogle(context: Activity) {
      if (isNetworkAvailable(context)) {
         viewModelScope.launch {
            handleAuthResult(signInWithGoogleUseCase(context))
         }
      } else handleAuthResult(AuthResult.Error(AuthError.NetworkError))
   }

   fun signInAnonymous() {
      if (isNetworkAvailable(application)) {
         viewModelScope.launch {
            handleAuthResult(signInAnonymousUseCase())
         }
      } else handleAuthResult(AuthResult.Error(AuthError.NetworkError))
   }

   private companion object {
      private const val TIMEOUT_MILLIS = 5_000L
      const val TAG = "AuthViewModel"
   }
}