package com.ad.cookgood.authentication.presentation

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import androidx.media3.ui.PlayerView
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
         it,
         onAnonymousSignInButtonClick = { vm.signInAnonymous() },
         onGoogleSignInButtonClick = { it -> vm.signInWithGoogle(it) },
         isLoadingGoogle = isLoadingGoogle,
         isLoadingAnonymous = isLoadingAnonymous
      )
   }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun AuthScreenContent(
   paddingValues: PaddingValues,
   onAnonymousSignInButtonClick: () -> Unit = {},
   onGoogleSignInButtonClick: (Activity) -> Unit = {},
   isLoadingGoogle: Boolean = true,
   isLoadingAnonymous: Boolean = true
) {

   val context = LocalContext.current

   val videoUri = Uri.Builder()
      .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE) // "android.resource"
      .authority(context.packageName) // Tên gói ứng dụng
      .appendPath(R.raw.clouds.toString()) // ID tài nguyên
      .build()

   val exoPlayer = remember {
      ExoPlayer.Builder(context).build().apply {
         val mediaItem = MediaItem.fromUri(videoUri)
         setMediaItem(mediaItem)
         prepare()
         playWhenReady = true
         repeatMode = ExoPlayer.REPEAT_MODE_ALL
      }
   }

   DisposableEffect(Unit) {
      onDispose {
         exoPlayer.release()
      }
   }

   AndroidView(
      factory = {
         // Tạo PlayerView trong AndroidView Factory
         PlayerView(it).apply {
            player = exoPlayer
            useController = false
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
         }
      },
      update = { view ->
         // Được gọi khi trạng thái Compose thay đổi (Không cần thiết cho ví dụ đơn giản này)
         // Ví dụ: view.player = newPlayer
      }
   )

   Column(
      modifier = Modifier
         .fillMaxSize()
         .padding(paddingValues),
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