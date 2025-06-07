package com.ad.cookgood.session_management.presentation

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.R
import com.ad.cookgood.authentication.domain.model.AuthError
import com.ad.cookgood.authentication.domain.model.AuthResult
import com.ad.cookgood.authentication.domain.usecase.LinkAnonymousUseCase
import com.ad.cookgood.authentication.domain.usecase.SignInWithGoogleUseCase
import com.ad.cookgood.profile.domain.GetCurrentUserUseCase
import com.ad.cookgood.profile.domain.UpdateUserProfileUseCase
import com.ad.cookgood.session_management.domain.SignOutUseCase
import com.ad.cookgood.shared.SnackBarUiState
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
   private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
   private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {

   private val _isAnonymous: MutableStateFlow<Boolean?> = MutableStateFlow(false)

   init {
      collectFlow()
   }

   val isAnonymous = _isAnonymous
      .stateIn(
         scope = viewModelScope,
         started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
         initialValue = false
      )

   private val _snackBarUiState = MutableStateFlow(SnackBarUiState())

   //expose state
   val snackBarUiState: StateFlow<SnackBarUiState> = _snackBarUiState

   private fun collectFlow() {
      viewModelScope.launch {
         getCurrentUserUseCase().collect {
            _isAnonymous.value = it?.isAnonymous
         }
      }
   }

   fun signOut() {
      isNetworkAvailable(application).let {
         if (it) {
            signOutUseCase()
         }
         else handleAuthResult(AuthResult.Error(AuthError.NetworkError))
      }
   }

   fun linkAnonymous(context: Activity) {
      if (isNetworkAvailable(application)) {
         viewModelScope.launch {
            handleAuthResult(linkAnonymousUseCase(context))
         }
      } else handleAuthResult(AuthResult.Error(AuthError.NetworkError))
   }

   fun signInWithGoogle(context: Activity) {
      if (isNetworkAvailable(context)) {
         viewModelScope.launch {
            handleAuthResult(signInWithGoogleUseCase(context))
         }
      } else handleAuthResult(AuthResult.Error(AuthError.NetworkError))
   }

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
            if (authResult.reason is AuthError.CredentialAlreadyInUse) {
               _snackBarUiState.value = _snackBarUiState.value.copy(
                  actionLabel = application.getString(R.string.auth_screen_title_bar),
               )
            }
         }

         AuthResult.Success -> {
            _snackBarUiState.value = _snackBarUiState.value.copy(
               message = application.getString(R.string.message_success),
               isError = false
            )
         }

         is AuthResult.LinkSuccess -> {
            _snackBarUiState.value = _snackBarUiState.value.copy(
               message = application.getString(R.string.link_anonymous_success),
               isError = false
            )
            viewModelScope.launch {
               updateUserProfileUseCase(authResult.name, authResult.url)
               collectFlow()
            }
         }
      }
   }

   fun onDismissSnackBar() {
      _snackBarUiState.value = _snackBarUiState.value.copy(
         showSnackBar = false,
         actionLabel = null
      )
   }

   private companion object {
      private const val TIMEOUT_MILLIS = 5_000L
      const val TAG = "SessionManagementViewModel"
   }
}