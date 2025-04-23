package com.ad.cookgood.recipes.presentation.entry

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.cookgood.R
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.InstructionUiState
import kotlinx.coroutines.launch

@Preview
@Composable
fun RecipeEntryScreen(
   modifier: Modifier = Modifier,
   navigateUp: () -> Unit = {},
   navigateBack: () -> Unit = {},
   vm: RecipeEntryViewModel = hiltViewModel(),
) {
   val snackBarHostState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()

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
         Box {
            Image(
               painter = painterResource(R.drawable.recipe_entry_image),
               contentDescription = null,
               modifier = Modifier.fillMaxWidth(),
            )
            Text(
               stringResource(R.string.add_image),
               Modifier.align(Alignment.BottomCenter),
               style = MaterialTheme.typography.titleLarge
            )
         }

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