package com.ad.cookgood.login.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.login.domain.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
   private val authRepository: AuthRepository
) : ViewModel() {

   private val _signInCheck = mutableStateOf(false)
   val signInCheck: State<Boolean> = _signInCheck

   init {
      if (authRepository.getCurrentUser() != null) _signInCheck.value = true
      else viewModelScope.launch {
         authRepository.createGuest()
         _signInCheck.value = true
      }
   }

}