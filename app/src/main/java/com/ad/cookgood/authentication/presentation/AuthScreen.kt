package com.ad.cookgood.authentication.presentation

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ad.cookgood.R
import com.ad.cookgood.authentication.domain.model.AuthResult
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
   modifier: Modifier = Modifier,
   vm: AuthViewModel,
   onSignInSuccess: () -> Unit
) {

   val snackBarHostState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()

   Scaffold(
      topBar = {
         TopAppBar(
            title = {
               Text(stringResource(R.string.auth_screen_title_bar))
            }
         )
      },
      snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
   ) {

      val authState by vm.authState.collectAsState()
      val currentUserid by vm.currentUserId.collectAsStateWithLifecycle(
         minActiveState = Lifecycle.State.RESUMED
      )

      currentUserid?.let {
         onSignInSuccess()
         vm.clearState()
      }

      when (authState) {
         is AuthResult.Success -> ""

         is AuthResult.Error -> {
            val error = (authState as AuthResult.Error).reason
            val message = stringResource(error.messageRes)
            LaunchedEffect(error) {
               scope.launch {
                  val result = snackBarHostState.showSnackbar(
                     message = message,
                     withDismissAction = true,
                     duration = SnackbarDuration.Short
                  )
                  when (result) {
                     SnackbarResult.Dismissed -> vm.clearState()
                     SnackbarResult.ActionPerformed -> ""
                  }
               }
            }
         }

         else -> {
            // Handle loading state or initial state
            Column(
               modifier
                  .padding(it)
                  .fillMaxSize(),
               horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.Center
            ) {
               val context = LocalContext.current
               OutlinedButton(onClick = { vm.signInAnonymous(context as Activity) }) {
                  Text(stringResource(R.string.auth_button_anonymous))
               }

               Spacer(Modifier.size(dimensionResource(R.dimen.padding_medium)))

               OutlinedButton(onClick = { vm.signInWithGoogle(context as Activity) }) {
                  Text(stringResource(R.string.google_signin))
                  Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))
                  Image(painterResource(R.drawable.google_logo_72), contentDescription = null)
               }

            }
         }
      }
   }
}
