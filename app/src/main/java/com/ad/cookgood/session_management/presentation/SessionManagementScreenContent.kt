package com.ad.cookgood.session_management.presentation

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ad.cookgood.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview
@Composable
fun SessionManagementScreenContent(
   modifier: Modifier = Modifier,
   isAnonymous: Boolean? = true,
   onSignOutSuccess: () -> Unit = {},
   navigateToProfileScree: () -> Unit = {},
   linkAnonymous: (Activity) -> Unit = {},
   scope: CoroutineScope = rememberCoroutineScope(),
   snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
   signOut: () -> Unit = {}
) {
   Column(
      modifier,
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      when (isAnonymous) {
         true -> {
            val context = LocalContext.current
            OutlinedButton(
               onClick = { linkAnonymous(context as Activity) },
               modifier = Modifier.width(250.dp)
            ) {
               Text(stringResource(R.string.link_anonymous))
            }
         }

         false -> {
            OutlinedButton(onClick = navigateToProfileScree, modifier = Modifier.width(250.dp)) {
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
      OutlinedButton(onClick = signOut, modifier = Modifier.width(250.dp)) {
         Text(stringResource(R.string.signout))
         Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))
         Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
      }
   }
}