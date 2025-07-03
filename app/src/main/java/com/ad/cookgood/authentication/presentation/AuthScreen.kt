package com.ad.cookgood.authentication.presentation

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.cookgood.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import com.google.android.exoplayer2.ui.StyledPlayerView
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
   val isLoadingGoogle by vm.isLoadingGoogle.collectAsState()
   val isLoadingAnonymous by vm.isLoadingAnonymous.collectAsState()

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
         onGoogleSignInButtonClick = { it -> vm.signInWithGoogle(it) },
         isLoadingGoogle = isLoadingGoogle,
         isLoadingAnonymous = isLoadingAnonymous
      )
   }
}

@Preview
@Composable
fun AuthScreenContent(
   modifier: Modifier = Modifier,
   onAnonymousSignInButtonClick: () -> Unit = {},
   onGoogleSignInButtonClick: (Activity) -> Unit = {},
   isLoadingGoogle: Boolean = true,
   isLoadingAnonymous: Boolean = true
) {

   val context = LocalContext.current
   val videoUri = getVideoUri(context)
   val exoPlayer = remember { context.buildExoPlayer(videoUri) }

   DisposableEffect(
      AndroidView(
         factory = { it.buildPlayerView(exoPlayer) },
         modifier = Modifier.fillMaxSize()
      )
   ) {
      onDispose {
         exoPlayer.release()
      }
   }

   Column(
      modifier,
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
   ) {
      val context = LocalContext.current
      Button(
         onClick = onAnonymousSignInButtonClick,
         modifier = Modifier.width(300.dp)
      ) {
         if (isLoadingAnonymous) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
         } else Text(stringResource(R.string.auth_button_anonymous))
      }

      Spacer(Modifier.size(dimensionResource(R.dimen.padding_medium)))

      Button(
         onClick = { onGoogleSignInButtonClick(context as Activity) },
         modifier = Modifier.width(300.dp)
      ) {
         if (isLoadingGoogle) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
         } else {
            Text(stringResource(R.string.google_signin))
            Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))
            Image(painterResource(R.drawable.google_logo_72), contentDescription = null)
         }
      }
   }
}

private fun Context.buildExoPlayer(uri: Uri) =
   ExoPlayer.Builder(this).build().apply {
      setMediaItem(MediaItem.fromUri(uri))
      repeatMode = Player.REPEAT_MODE_ALL
      playWhenReady = true
      prepare()
   }

private fun Context.buildPlayerView(exoPlayer: ExoPlayer) =
   StyledPlayerView(this).apply {
      player = exoPlayer
      layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
      useController = false
      resizeMode = RESIZE_MODE_ZOOM
   }

private fun getVideoUri(context: Context): Uri {
   val rawId = context.resources.getIdentifier("clouds", "raw", context.packageName)
   val videoUri = "android.resource://${context.packageName}/$rawId"
   return videoUri.toUri()
}