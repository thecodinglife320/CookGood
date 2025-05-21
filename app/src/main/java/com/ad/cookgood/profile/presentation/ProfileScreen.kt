package com.ad.cookgood.profile.presentation

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.ad.cookgood.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
   modifier: Modifier = Modifier,
   profileViewModel: ProfileViewModel
) {

   val snackBarHostState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()

   Scaffold(
      topBar = {
         TopAppBar(
            title = { Text(stringResource(R.string.profile_screen_title_bar)) }
         )
      },
      snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
   ) {

      val profileUiState by profileViewModel.profileUiState.collectAsState()

      Column(
         modifier = modifier
            .fillMaxSize()
            .padding(it),
         horizontalAlignment = Alignment.CenterHorizontally,
         verticalArrangement = Arrangement.Center
      ) {
         when (profileUiState) {

            ProfileUiState.Loading -> {
               CircularProgressIndicator()
            }

            is ProfileUiState.Success -> {
               val context = LocalContext.current
               val profileData = profileUiState as ProfileUiState.Success

//               Text(profileData.name ?: "ko ten")
//
//               CoilImage(
//                  uri = profileData.url,
//                  modifier = Modifier
//                     .size(100.dp)
//                     .clip(CircleShape)
//               )
               Text(profileData.toString())

               if (profileData.isAnonymous) OutlinedButton(onClick = {
                  profileViewModel.signIn(
                     context as Activity
                  )
               }) { Text("lien ket tai khoan") }
               else OutlinedButton(onClick = profileViewModel::signOut) { Text("dang xuat") }
            }
         }
      }
   }

   profileViewModel.error.value?.let {
      scope.launch {
         snackBarHostState.showSnackbar(
            message = it,
            withDismissAction = true,
            duration = SnackbarDuration.Short
         )
      }
   }
}
