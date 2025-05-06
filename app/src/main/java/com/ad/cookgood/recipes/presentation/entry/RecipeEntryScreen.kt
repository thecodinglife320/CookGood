package com.ad.cookgood.recipes.presentation.entry

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.ad.cookgood.R
import com.ad.cookgood.captureimage.presentation.CameraPreview
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.InstructionUiState
import com.ad.cookgood.shared.CoilImage
import kotlinx.coroutines.launch

@Composable
fun RecipeEntryScreen(
   modifier: Modifier = Modifier,
   navigateUp: () -> Unit = {},
   navigateBack: () -> Unit = {},
   vm: RecipeEntryViewModel = hiltViewModel(),

   ) {
   val snackBarHostState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()

   if (vm.showPopUp.value) {
      Popup(
         properties = PopupProperties(focusable = true)
      ) {
         CameraPreview(
            modifier = Modifier.height(300.dp),
            stopCamera = vm::stopCamera,
            startCamera = { a, b -> vm.startCamera(a, b) },
            takePhoto = vm::takePhoto,
         )
      }
   }

   Scaffold(
      snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
      modifier = modifier,
      topBar = {
         RecipeEntryToolBar(
            navigateUp = navigateUp,
            saveRecipe = vm::saveRecipe
         )
      }
   ) {

      Column(
         modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(it)
      ) {

         RecipePhoto(
            showPopUp1 = vm.showPopUp1.value,
            onTakePhotoRecipe = vm::onTakePhotoRecipe,
            startCamera = { a, b -> vm.startCamera(a, b) },
            stopCamera = vm::stopCamera,
            onOpenCamera = vm::onOpenCamera,
            uri = vm.uriRecipePhoto
         )

         RecipeEntrySection1(
            onTitleChange = vm::onTitleChange,
            onBriefChange = vm::onBriefChange,
            keyboardOptions = KeyboardOptions.Default.copy(
               imeAction = ImeAction.Next,
               keyboardType = KeyboardType.Text
            )
         )

         RecipeEntrySection2(
            onServingChange = vm::onServingChange,
            onHourChange = vm::onHourChange,
            onMinuteChange = vm::onMinuteChange,
            keyboardOptions = KeyboardOptions.Default.copy(
               imeAction = ImeAction.Next,
               keyboardType = KeyboardType.Number
            )
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
            takePhotoForInstruction = {
               vm.takePhotoForInstruction(it)
            },
         )
      }
   }

   vm.successMessage.value?.let {
      scope.launch {
         val result = snackBarHostState.showSnackbar(
            message = it,
            withDismissAction = true,
            duration = SnackbarDuration.Short
         )
         when (result) {
            SnackbarResult.Dismissed -> navigateBack()
            SnackbarResult.ActionPerformed -> ""
         }
      }
   }

   vm.error.value?.let {
      scope.launch {
         snackBarHostState.showSnackbar(
            message = it,
            withDismissAction = true,
            duration = SnackbarDuration.Short
         )
      }
   }
}

@Composable
fun RecipePhoto(
   modifier: Modifier = Modifier,
   showPopUp1: Boolean = false,
   onTakePhotoRecipe: () -> Unit,
   startCamera: (LifecycleOwner, androidx.camera.core.Preview.SurfaceProvider) -> Unit,
   stopCamera: () -> Unit,
   onOpenCamera: () -> Unit,
   uri: Uri?
) {
   Box(modifier.clickable(onClick = onOpenCamera)) {
      if (showPopUp1) {
         Popup(
            properties = PopupProperties(focusable = true)
         ) {
            CameraPreview(
               modifier = Modifier.height(300.dp),
               stopCamera = stopCamera,
               startCamera = { a, b -> startCamera(a, b) },
               takePhoto = onTakePhotoRecipe,
            )
         }
      }
      CoilImage(
         uri = uri,
         modifier = Modifier
            .width(300.dp)
            .height(200.dp)
      )
      Text(
         stringResource(R.string.add_image),
         Modifier.align(Alignment.BottomCenter),
         style = MaterialTheme.typography.titleLarge
      )
   }
}