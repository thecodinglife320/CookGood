package com.ad.cookgood.authentication.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

@Composable
fun AuthScreen(
   authViewModel: AuthViewModel,
   onSignInSuccess: () -> Unit
) {

   val signInCheck by authViewModel.signInCheck

   if (signInCheck) onSignInSuccess()
}
