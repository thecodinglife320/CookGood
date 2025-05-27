package com.ad.cookgood.session_management.presentation

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.authentication.domain.model.AuthError
import com.ad.cookgood.authentication.domain.model.AuthResult
import com.ad.cookgood.authentication.domain.usecase.LinkAnonymousUseCase
import com.ad.cookgood.authentication.domain.usecase.SignInWithGoogleUseCase
import com.ad.cookgood.profile.domain.GetCurrentUserUseCase
import com.ad.cookgood.session_management.domain.SignOutUseCase
import com.ad.cookgood.util.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionManagementViewModel @Inject constructor(
   private val signOutUseCase: SignOutUseCase,
   private val getCurrentUserUseCase: GetCurrentUserUseCase,
   private val linkAnonymousUseCase: LinkAnonymousUseCase,
   private val application: Application,
   private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {

   private val _isAnonymous: MutableStateFlow<Boolean?> = MutableStateFlow(null)

   init {
      collectFlow()
   }

   val isAnonymous = _isAnonymous
      .stateIn(
         scope = viewModelScope,
         started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
         initialValue = false
      )

   private val _authState = MutableStateFlow<AuthResult?>(null)
   val authState: StateFlow<AuthResult?> = _authState

   private fun collectFlow() {
      viewModelScope.launch {
         getCurrentUserUseCase().collect {
            _isAnonymous.value = it?.isAnonymous
         }
      }
   }

   fun signOut() {
      isNetworkAvailable(application).let {
         if (it) signOutUseCase()
         else _authState.value = AuthResult.Error(AuthError.NetworkError)
      }
   }

   fun linkAnonymous(context: Activity) {
      if (isNetworkAvailable(application)) {
         viewModelScope.launch {
            _authState.value = linkAnonymousUseCase(context)
            collectFlow()
         }
      } else _authState.value = AuthResult.Error(AuthError.NetworkError)
   }

   fun clearState() {
      _authState.value = null
   }

   fun signInWithGoogle(context: Activity) {
      if (isNetworkAvailable(context)) {
         viewModelScope.launch {
            val result = signInWithGoogleUseCase(context)
            _authState.value = result
         }
      } else _authState.value = AuthResult.Error(AuthError.NetworkError)
   }

   private companion object {
      private const val TIMEOUT_MILLIS = 5_000L
      const val TAG = "SessionManagementViewModel"
   }
}