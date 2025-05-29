package com.ad.cookgood.recipes.presentation.entry

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.cookgood.R
import com.ad.cookgood.captureimage.presentation.CameraPreview
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.InstructionUiState
import com.ad.cookgood.shared.CoilImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun RecipeEntryScreen(
   modifier: Modifier = Modifier,
   navigateUp: () -> Unit = {},
   navigateBack: () -> Unit = {},
   vm: RecipeEntryViewModel = hiltViewModel()

) {
   val snackBarHostState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()
   val keyboardController = LocalSoftwareKeyboardController.current
   val surfaceRequest by vm.surfaceRequest.collectAsState()
   val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
   val snackBarUiState by vm.snackBarUiState.collectAsState()
   val dialogUiState by vm.dialogUiState.collectAsState()
   val context = LocalContext.current

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

   if (vm.showPopUp.value) {
      Popup(
         properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
         ),
         onDismissRequest = {
            vm.onDismissPopUp()
         }
      ) {
         CameraPreview(
            modifier = Modifier.height(600.dp),
            bindToCamera = vm::bindToCamera,
            surfaceRequest = surfaceRequest,
            takePhoto = vm::onTakePhotoInstruction,
         )
      }
   }

   if (vm.showPopUp1.value) {
      Popup(
         properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
         ),
         onDismissRequest = {
            vm.onDismissPopUp()
         }
      ) {
         CameraPreview(
            modifier = Modifier.height(600.dp),
            bindToCamera = vm::bindToCamera,
            surfaceRequest = surfaceRequest,
            takePhoto = vm::onTakePhotoRecipe,
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
      modifier = modifier.imePadding(),
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

      Column(
         modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(it)
      ) {

         RecipePhoto(
            onPrepareTakePhotoRecipe = { vm.onPrepareTakePhotoRecipe(cameraPermissionState) },
            uri = vm.uriRecipePhoto
         )

         RecipeEntrySection1(
            onTitleChange = vm::onTitleChange,
            onBriefChange = vm::onBriefChange,
            keyboardOptions = KeyboardOptions.Default.copy(
               imeAction = ImeAction.Next,
               keyboardType = KeyboardType.Text
            ),
            title = vm.recipeUiState.value.title,
            brief = vm.recipeUiState.value.brief
         )

         RecipeEntrySection2(
            onServingChange = vm::onServingChange,
            onHourChange = vm::onHourChange,
            onMinuteChange = vm::onMinuteChange,
            keyboardOptions = KeyboardOptions.Default.copy(
               imeAction = ImeAction.Next,
               keyboardType = KeyboardType.Number
            ),
            serving = vm.recipeUiState.value.servings,
            hour = vm.recipeUiState.value.cookTimeHours,
            minute = vm.recipeUiState.value.cookTimeMinutes,
         )

         //nhap nguyen lieu
         RecipeEntrySection3(
            addCommonUiState = {
               vm.addCommonUiState(IngredientUiState(id = System.currentTimeMillis().toInt()))
            },
            removeCommonUiState = {
               vm.removeCommonUiState(it)
            },
            updateCommonUiState = { a, b ->
               vm.updateCommonUiState(a, b)
            },
            commonUiStates = vm.ingredientUiStates.value,
            textRes = R.string.nguyen_lieu,
            buttonTextRes = R.string.them_nguyen_lieu,
            label = R.string.ingredient_entry_label,
            placeHolder = R.string.ingredient_entry_place_holder,
         )

         //nhap buoc lam
         RecipeEntrySection3(
            textRes = R.string.cach_lam,
            buttonTextRes = R.string.them_buoc_lam,
            addCommonUiState = {
               vm.addCommonUiState(InstructionUiState(id = System.currentTimeMillis().toInt()))
            },
            removeCommonUiState = {
               vm.removeCommonUiState(it)
            },
            updateCommonUiState = { a, b ->
               vm.updateCommonUiState(a, b)
            },
            commonUiStates = vm.instructionUiStates.value,
            label = R.string.instruction_entry_label,
            placeHolder = R.string.instruction_entry_placeholder,
            onPrepareTakePhotoInstruction = {
               vm.onPrepareTakePhotoInstruction(it, cameraPermissionState)
            },
         )
      }
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