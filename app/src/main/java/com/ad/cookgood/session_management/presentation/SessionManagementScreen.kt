package com.ad.cookgood.session_management.presentation

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionManagementScreen(
   onSignOutSuccess: () -> Unit,
   navigateToProfileScree: () -> Unit
) {

   val vm: SessionManagementViewModel = hiltViewModel()
   val snackBarHostState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()
   val snackBarUiState by vm.snackBarUiState.collectAsState()
   val context = LocalContext.current
   val isAnonymous by vm.isAnonymous.collectAsState()

   if (snackBarUiState.showSnackBar) {
      SideEffect {
         scope.launch {
            val result = snackBarHostState.showSnackbar(
               message = snackBarUiState.message,
               actionLabel = snackBarUiState.actionLabel,
               withDismissAction = true
            )
            when (result) {
               SnackbarResult.Dismissed -> vm.onDismissSnackBar()
               SnackbarResult.ActionPerformed -> {
                  vm.signInWithGoogle(context as Activity)
                  vm.onDismissSnackBar()
               }
            }
         }
      }
   }

   Scaffold(
      snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
   ) {
      SessionManagementScreenContent(
         modifier = Modifier
            .fillMaxSize()
            .padding(it),
         isAnonymous = isAnonymous,
         onSignOutSuccess = onSignOutSuccess,
         navigateToProfileScree = navigateToProfileScree,
         linkAnonymous = {
            vm.linkAnonymous(it)
         },
         scope = scope,
         snackBarHostState = snackBarHostState,
         signOut = vm::signOut
      )
   }
}