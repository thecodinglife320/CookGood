package com.ad.cookgood.profile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.cookgood.R
import com.ad.cookgood.shared.CoilImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
   navigateUp: () -> Unit
) {

   val vm = hiltViewModel<ProfileViewModel>()
   val snackBarHostState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()
   val snackBarUiState by vm.snackBarUiState.collectAsState()
   val profileUiState by vm.profileUiState.collectAsState()

   if (snackBarUiState.showSnackBar) {
      SideEffect {
         scope.launch {
            val result = snackBarHostState.showSnackbar(
               message = snackBarUiState.message,
               actionLabel = snackBarUiState.actionLabel,
               withDismissAction = true,
            )
            when (result) {
               SnackbarResult.Dismissed -> vm.onDismissSnackBar()
               SnackbarResult.ActionPerformed -> ""
            }
         }
      }
   }

   Scaffold(
      topBar = {
         TopAppBar(
            title = { Text(stringResource(R.string.profile_screen_title_bar)) },
            navigationIcon = {
               IconButton(onClick = navigateUp) {
                  Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
               }
            }
         )
      },
      snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
   ) {
      ProfileScreenContent(
         Modifier
            .padding(it)
            .fillMaxSize(),
         profileUiState = profileUiState,
         onNameChange = {
            vm.onNameChange(it)
         },
         updateUserProfile = vm::updateUserProfile
      )
   }
}

@Preview
@Composable
fun ProfileScreenContent(
   modifier: Modifier = Modifier,
   profileUiState: ProfileUiState = ProfileUiState.Success(
      email = "ttll@gmail.com",
      name = "dap dep trai",
      url = "".toUri()
   ),
   onNameChange: (String) -> Unit = {},
   updateUserProfile: () -> Unit = {}
) {
   Column(
      modifier
         .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      when (val profileUiState = profileUiState) {
         ProfileUiState.Loading -> CircularProgressIndicator()
         is ProfileUiState.Success -> {
            Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))

            CoilImage(
               uri = profileUiState.url,
               modifier = Modifier
                  .size(100.dp)
                  .clip(CircleShape)
            )

            Spacer(Modifier.size(32.dp))

            OutlinedTextField(
               value = profileUiState.name ?: "",
               onValueChange = onNameChange,
               label = { Text(stringResource(R.string.display_name)) },
               modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))

            OutlinedTextField(
               value = profileUiState.email ?: "",
               onValueChange = {},
               label = { Text(stringResource(R.string.email)) },
               enabled = false,
               modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(32.dp))
            OutlinedButton(updateUserProfile) {
               Text(stringResource(R.string.update_profile))
            }

            Text(profileUiState.toString())
         }
      }

   }
}

