package com.ad.cookgood.authentication.presentation

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.cookgood.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
   onSignInSuccess: () -> Unit
) {

   val snackBarHostState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()
   val vm: AuthViewModel = hiltViewModel<AuthViewModel>()
   val snackBarUiState by vm.snackBarUiState.collectAsState()

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

      if (snackBarUiState.showSnackBar) {
         SideEffect {
            scope.launch {
               val result = snackBarHostState.showSnackbar(
                  message = snackBarUiState.message,
                  withDismissAction = true,
               )
               when (result) {
                  SnackbarResult.Dismissed -> if (snackBarUiState.isError) {
                     vm.onDismissSnackBar()
                  } else {
                     vm.onDismissSnackBar()
                     onSignInSuccess()
                  }

                  SnackbarResult.ActionPerformed -> ""
               }
            }
         }
      }

      AuthScreenContent(
         modifier = Modifier
            .padding(it)
            .fillMaxSize(),
         onAnonymousSignInButtonClick = { vm.signInAnonymous() },
         onGoogleSignInButtonClick = { vm.signInWithGoogle(it) },
      )
   }
}

@Preview
@Composable
fun AuthScreenContent(
   modifier: Modifier = Modifier,
   onAnonymousSignInButtonClick: () -> Unit = {},
   onGoogleSignInButtonClick: (Activity) -> Unit = {},
) {
   Column(
      modifier,
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
   ) {
      val context = LocalContext.current
      OutlinedButton(
         onClick = onAnonymousSignInButtonClick,
         modifier = Modifier.width(300.dp)
      ) {
         Text(stringResource(R.string.auth_button_anonymous))
      }

      Spacer(Modifier.size(dimensionResource(R.dimen.padding_medium)))

      OutlinedButton(
         onClick = { onGoogleSignInButtonClick(context as Activity) },
         modifier = Modifier.width(300.dp)
      ) {
         Text(stringResource(R.string.google_signin))
         Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))
         Image(painterResource(R.drawable.google_logo_72), contentDescription = null)
      }

   }
}
