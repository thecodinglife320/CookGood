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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ad.cookgood.R
import com.ad.cookgood.shared.CoilImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
   modifier: Modifier = Modifier,
   profileViewModel: ProfileViewModel,
   navigateUp: () -> Unit
) {

   val snackBarHostState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()
   val error by profileViewModel.error

   LaunchedEffect(error) {
      error?.let {
         scope.launch {
            val result = snackBarHostState.showSnackbar(
               message = it,
               withDismissAction = true,
               duration = SnackbarDuration.Short
            )
            when (result) {
               SnackbarResult.Dismissed -> ""
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

      val profileUiState by profileViewModel.profileUiState.collectAsState()

      Column(
         modifier = modifier
            .fillMaxSize()
            .padding(it)
            .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
         horizontalAlignment = Alignment.CenterHorizontally,
      ) {
         when (profileUiState) {

            ProfileUiState.Loading -> {
               CircularProgressIndicator()
            }

            is ProfileUiState.Success -> {
               val context = LocalContext.current
               val profileData = profileUiState as ProfileUiState.Success

               Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))

               CoilImage(
                  uri = profileData.url,
                  modifier = Modifier
                     .size(100.dp)
                     .clip(CircleShape)
               )

               Spacer(Modifier.size(32.dp))

               OutlinedTextField(
                  value = profileData.name ?: "",
                  onValueChange = profileViewModel::onNameChange,
                  label = { Text(stringResource(R.string.display_name)) },
                  modifier = Modifier.fillMaxWidth()
               )

               Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))

               OutlinedTextField(
                  value = profileData.email ?: "",
                  onValueChange = {},
                  label = { Text(stringResource(R.string.email)) },
                  enabled = false,
                  modifier = Modifier.fillMaxWidth()
               )

               Spacer(Modifier.size(32.dp))
               OutlinedButton(profileViewModel::updateUserProfile) {
                  Text(stringResource(R.string.update_profile))
               }

               Text(profileData.toString())
            }
         }
      }
   }
}

