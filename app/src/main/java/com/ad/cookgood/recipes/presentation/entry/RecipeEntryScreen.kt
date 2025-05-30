package com.ad.cookgood.recipes.presentation.entry

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.ad.cookgood.R
import com.ad.cookgood.captureimage.presentation.CameraPreview
import com.ad.cookgood.shared.CoilImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun RecipeEntryScreen(
   navigateUp: () -> Unit = {},
   navigateBack: () -> Unit = {},
   vm: RecipeEntryViewModel

) {
   val snackBarHostState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()
   val keyboardController = LocalSoftwareKeyboardController.current
   val surfaceRequest by vm.surfaceRequest.collectAsState()
   val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
   val snackBarUiState by vm.snackBarUiState.collectAsState()
   val dialogUiState by vm.dialogUiState.collectAsState()
   val context = LocalContext.current
   val cameraPreviewUiState by vm.cameraPreviewUiState.collectAsState()
   val recipeUiState by vm.recipeUiState
   val ingredientUiStates by vm.ingredientUiStates
   val instructionUiStates by vm.instructionUiStates

   if (dialogUiState.showDialog) {
      CameraPermissionDialog(
         onDismissRequest = { vm.onDismissDialog() },
         onConfirmClick = if (dialogUiState.shouldShowRationale) {
            {
               val intent = Intent(
                  Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                  Uri.fromParts("package", context.packageName, null)
               )
               context.startActivity(intent)
               vm.onDismissDialog()
            }
         } else {
            {
               cameraPermissionState.launchPermissionRequest()
               vm.onDismissDialog()
            }
         },
         onDismissClick = { vm.onDismissDialog() },
         message = dialogUiState.message,
      )
   }

   if (cameraPreviewUiState.showCameraPreview) {
      Popup(
         properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
         ),
         onDismissRequest = {
            vm.onDismissCameraPreview()
         }
      ) {
         CameraPreview(
            modifier = Modifier.height(600.dp),
            bindToCamera = vm::bindToCamera,
            surfaceRequest = surfaceRequest,
            takePhoto = if (cameraPreviewUiState.isCaptureForRecipe) {
               {
                  vm.onTakePhotoRecipe()
                  vm.onDismissCameraPreview()
               }
            } else {
               {
                  vm.onTakePhotoInstruction()
                  vm.onDismissCameraPreview()
               }
            }
         )
      }
   }

   if (snackBarUiState.showSnackBar) {
      SideEffect {
         scope.launch {
            val result = snackBarHostState.showSnackbar(
               message = snackBarUiState.message,
               withDismissAction = true,
               duration = SnackbarDuration.Short
            )
            when (result) {
               SnackbarResult.Dismissed -> if (snackBarUiState.isError) {
                  vm.onDismissSnackBar()
               } else {
                  navigateBack()
               }

               SnackbarResult.ActionPerformed -> ""
            }
         }
      }
   }

   Scaffold(
      snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
      modifier = Modifier.imePadding(),
      topBar = {
         TopAppBar(
            title = {},
            navigationIcon = {
               IconButton(onClick = navigateUp) {
                  Icon(
                     imageVector = Icons.Default.Close,
                     contentDescription = "Close"
                  )
               }
            },
            actions = {
               IconButton(
                  onClick = {
                     keyboardController?.hide()
                     vm.saveRecipe()
                  }
               ) {
                  Icon(
                     imageVector = Icons.Default.Save,
                     contentDescription = "Save"
                  )
               }
            },
         )
      }
   ) {
      RecipeEntryScreenContent(
         modifier = Modifier
            .padding(it)
            .verticalScroll(rememberScrollState()),
         recipeUiState = recipeUiState,
         onPrepareTakePhotoRecipe = { vm.onPrepareTakePhotoRecipe(cameraPermissionState) },
         onTitleChange = vm::onTitleChange,
         onBriefChange = vm::onBriefChange,
         onServingChange = vm::onServingChange,
         onHourChange = vm::onHourChange,
         onMinuteChange = vm::onMinuteChange,
         addCommonUiState = {
            vm.addCommonUiState(it)
         },
         removeCommonUiState = {
            vm.removeCommonUiState(it)
         },
         updateCommonUiState = { a, b ->
            vm.updateCommonUiState(a, b)
         },
         ingredientUiStates = ingredientUiStates,
         instructionUiStates = instructionUiStates,
         onPrepareTakePhotoInstruction = { a, b ->
            vm.onPrepareTakePhotoInstruction(a, b!!)
         },
         cameraPermissionState = cameraPermissionState,
      )
   }
}

@Composable
fun RecipePhoto(
   modifier: Modifier = Modifier,
   onPrepareTakePhotoRecipe: () -> Unit,
   uri: Uri?
) {
   Box(modifier.clickable(onClick = onPrepareTakePhotoRecipe)) {
      CoilImage(
         uri = uri,
         modifier = Modifier
            //.width(350.dp)
            .height(300.dp)
      )
      Text(
         stringResource(R.string.add_image),
         Modifier.align(Alignment.BottomCenter),
         style = MaterialTheme.typography.titleLarge
      )
   }
}