package com.ad.cookgood.session_management.presentation

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import com.ad.cookgood.R
import com.ad.cookgood.authentication.domain.model.AuthError
import com.ad.cookgood.authentication.domain.model.AuthResult
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionManagementScreen(
   modifier: Modifier = Modifier,
   vm: SessionManagementViewModel,
   onSignOutSuccess: () -> Unit,
   navigateUp: () -> Unit
) {

   val snackBarHostState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()

   Scaffold(
      topBar = {
         TopAppBar(
            title = {
               Text(stringResource(R.string.session_management_screen_title_bar))
            },
            navigationIcon = {
               IconButton(onClick = navigateUp) {
                  Icon(
                     imageVector = Icons.Default.ArrowBackIosNew,
                     contentDescription = null
                  )
               }
            }
         )
      }, snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
   ) {
      Column(
         modifier
            .padding(it)
            .fillMaxSize(),
         verticalArrangement = Arrangement.Center,
         horizontalAlignment = Alignment.CenterHorizontally
      ) {

         val isAnonymous by vm.isAnonymous.collectAsState()
         val authState by vm.authState.collectAsState()

         when (isAnonymous) {
            true -> {
               val context = LocalContext.current
               OutlinedButton(onClick = { vm.linkAnonymous(context as Activity) }) {
                  Text(stringResource(R.string.link_anonymous))
               }
            }

            false -> ""
            null -> {
               val message = stringResource(R.string.signout_success)
               LaunchedEffect(Unit) {
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
               val actionLabel =
                  if (error is AuthError.CredentialAlreadyInUse) stringResource(R.string.auth_screen_title_bar) else null
               val context = LocalContext.current

               LaunchedEffect(error) {
                  scope.launch {
                     val result = snackBarHostState.showSnackbar(
                        message = message,
                        actionLabel = actionLabel,
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                     )
                     when (result) {
                        SnackbarResult.Dismissed -> vm.clearState()
                        SnackbarResult.ActionPerformed -> {
                           vm.signInWithGoogle(context as Activity)
                        }
                     }
                  }
               }
            }

            AuthResult.Success -> {
               val message = stringResource(R.string.link_anonymous_success)
               LaunchedEffect(Unit) {
                  scope.launch {
                     snackBarHostState.showSnackbar(message)
                  }
               }
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