package com.ad.cookgood.recipes.presentation.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.ad.cookgood.R
import com.ad.cookgood.recipes.presentation.state.CommonUiState
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.InstructionUiState
import com.ad.cookgood.recipes.presentation.state.RecipeUiState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun RecipeEntryScreenContent(
   modifier: Modifier = Modifier,
   recipeUiState: RecipeUiState = RecipeUiState(),
   onPrepareTakePhotoRecipe: () -> Unit = {},
   onTitleChange: (String) -> Unit = {},
   onBriefChange: (String) -> Unit = {},
   onServingChange: (String) -> Unit = {},
   onHourChange: (String) -> Unit = {},
   onMinuteChange: (String) -> Unit = {},
   addCommonUiState: (CommonUiState) -> Unit = {},
   removeCommonUiState: (CommonUiState) -> Unit = {},
   updateCommonUiState: (CommonUiState, String) -> Unit = { _, _ -> },
   ingredientUiStates: List<IngredientUiState> = listOf(IngredientUiState(1, "d")),
   instructionUiStates: List<InstructionUiState> = listOf(InstructionUiState(1, "d")),
   onPrepareTakePhotoInstruction: (Int, PermissionState?) -> Unit = { _, _ -> },
   cameraPermissionState: PermissionState? = null
) {
   Column(modifier) {

      RecipePhoto(
         onPrepareTakePhotoRecipe = onPrepareTakePhotoRecipe,
         uri = recipeUiState.uri
      )

      RecipeEntrySection1(
         onTitleChange = onTitleChange,
         onBriefChange = onBriefChange,
         keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
         ),
         title = recipeUiState.title,
         brief = recipeUiState.brief
      )

      RecipeEntrySection2(
         onServingChange = onServingChange,
         onHourChange = onHourChange,
         onMinuteChange = onMinuteChange,
         keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
         ),
         serving = recipeUiState.servings,
         hour = recipeUiState.cookTimeHours,
         minute = recipeUiState.cookTimeMinutes,
      )

      //nhap nguyen lieu
      RecipeEntrySection3(
         addCommonUiState = {
            addCommonUiState(IngredientUiState(id = System.currentTimeMillis().toInt()))
         },
         removeCommonUiState = {
            removeCommonUiState(it)
         },
         updateCommonUiState = { a, b ->
            updateCommonUiState(a, b)
         },
         commonUiStates = ingredientUiStates,
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
            addCommonUiState(InstructionUiState(id = System.currentTimeMillis().toInt()))
         },
         removeCommonUiState = {
            removeCommonUiState(it)
         },
         updateCommonUiState = { a, b ->
            updateCommonUiState(a, b)
         },
         commonUiStates = instructionUiStates,
         label = R.string.instruction_entry_label,
         placeHolder = R.string.instruction_entry_placeholder,
         onPrepareTakePhotoInstruction = {
            onPrepareTakePhotoInstruction(it, cameraPermissionState)
         },
      )
   }
}