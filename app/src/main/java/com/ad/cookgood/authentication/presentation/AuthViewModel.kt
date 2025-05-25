package com.ad.cookgood.authentication.presentation

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
   getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

   private val _authState = MutableStateFlow<AuthResult?>(null)
   val authState: StateFlow<AuthResult?> = _authState

   val currentUserId = getCurrentUserUseCase()
      .map {
         it?.uid
      }
      .stateIn(
         scope = viewModelScope,
         started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
         initialValue = null
      )

   fun signInWithGoogle(context: Activity) {
      if (isNetworkAvailable(context)) {
         viewModelScope.launch {
            val result = signInWithGoogleUseCase(context)
            _authState.value = result
         }
      } else _authState.value = AuthResult.Error(AuthError.NetworkError)
   }

   fun signInAnonymous(context: Activity) {
      if (isNetworkAvailable(context)) {
         viewModelScope.launch {
            val result = signInAnonymousUseCase()
            _authState.value = result
         }
      } else _authState.value = AuthResult.Error(AuthError.NetworkError)
   }

   fun clearState() {
      _authState.value = null
   }

   private companion object {
      private const val TIMEOUT_MILLIS = 5_000L
   }
}