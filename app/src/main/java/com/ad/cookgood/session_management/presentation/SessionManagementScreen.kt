package com.ad.cookgood.session_management.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import com.ad.cookgood.R
import com.ad.cookgood.authentication.domain.model.AuthError
import com.ad.cookgood.authentication.domain.model.AuthResult
import com.ad.cookgood.authentication.domain.model.LinkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionManagementScreen(
   modifier: Modifier = Modifier,
   vm: SessionManagementViewModel,
   onSignOutSuccess: () -> Unit,
   navigateToProfileScree: () -> Unit
) {

   val snackBarHostState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()

   Scaffold(
      snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
   ) {
      Column(
         modifier
            .fillMaxSize(),
         verticalArrangement = Arrangement.Center,
         horizontalAlignment = Alignment.CenterHorizontally
      ) {

         val isAnonymous by vm.isAnonymous.collectAsState()
         val authState by vm.authState.collectAsState()
         val linkState by vm.linkState.collectAsState()

         when (isAnonymous) {
            true -> {
               val context = LocalContext.current
               OutlinedButton(onClick = { vm.linkAnonymous(context as Activity) }) {
                  Text(stringResource(R.string.link_anonymous))
               }
            }

            false -> {
               OutlinedButton(onClick = navigateToProfileScree) {
                  Icon(Icons.Default.AccountCircle, contentDescription = null)
                  Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))
                  Text(stringResource(R.string.user_profile))
               }
            }

            null -> {
               val message = stringResource(R.string.signout_success)
               SideEffect {
                  scope.launch {
                     val result = snackBarHostState.showSnackbar(
                        message = message,
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                     )
                     when (result) {
                        SnackbarResult.Dismissed -> onSignOutSuccess()
                        SnackbarResult.ActionPerformed -> ""
                     }
                  }
               }
            }
         }

         when (authState) {
            is AuthResult.Error -> {
               val error = (authState as AuthResult.Error).reason
               val message = stringResource(error.messageRes)
               ErrorMessage(
                  message = message,
                  scope = scope,
                  snackBarHostState = snackBarHostState,
               )
            }

            AuthResult.Success -> {
               SuccessMessage(
                  message = stringResource(R.string.sign_in_google_success),
                  scope = scope,
                  snackBarHostState = snackBarHostState
               )
            }

            null -> ""
         }

         when (linkState) {
            is LinkResult.Error -> {
               Log.d("SessionManagementScreen", "aaa")
               val error = (linkState as LinkResult.Error).reason
               val message = stringResource(error.messageRes)
               val actionLabel =
                  if (error is AuthError.CredentialAlreadyInUse) stringResource(R.string.auth_screen_title_bar) else null
               val context = LocalContext.current

               ErrorMessage(
                  message = message,
                  scope = scope,
                  snackBarHostState = snackBarHostState,
                  actionLabel = actionLabel,
                  onActionPerformed = { vm.signInWithGoogle(context as Activity) },
                  onDismissed = { vm.clearState() }
               )
            }

            is LinkResult.Success -> {
               SuccessMessage(
                  message = stringResource(R.string.link_anonymous_success),
                  scope = scope,
                  snackBarHostState = snackBarHostState
               )
            }

            null -> ""
         }

         OutlinedButton(onClick = vm::signOut) {
            Text(stringResource(R.string.signout))
            Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))
            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
         }
      }
   }
}

@Composable
fun SuccessMessage(
   message: String,
   scope: CoroutineScope,
   snackBarHostState: SnackbarHostState
) {
   SideEffect {
      scope.launch {
         snackBarHostState.showSnackbar(message)
      }
   }
}

@Composable
fun ErrorMessage(
   message: String,
   scope: CoroutineScope,
   snackBarHostState: SnackbarHostState,
   actionLabel: String? = null,
   onActionPerformed: () -> Unit = {},
   onDismissed: () -> Unit = {}
) {

   SideEffect {
      scope.launch {
         val result = snackBarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = true,
         )
         when (result) {
            SnackbarResult.Dismissed -> onDismissed()
            SnackbarResult.ActionPerformed -> onActionPerformed()
         }
      }
   }
}