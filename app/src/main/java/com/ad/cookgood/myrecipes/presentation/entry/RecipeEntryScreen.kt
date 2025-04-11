package com.ad.cookgood.myrecipes.presentation.entry

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ad.cookgood.R
import com.ad.cookgood.myrecipes.presentation.state.IngredientUiState
import com.ad.cookgood.myrecipes.presentation.state.InstructionUiState
import com.ad.cookgood.myrecipes.presentation.state.RecipeUiState
import kotlinx.coroutines.launch

@Preview
@Composable
fun RecipeEntryScreen(
   modifier: Modifier = Modifier,
   recipeUiState: RecipeUiState = RecipeUiState(),
   ingredientsUiState: List<IngredientUiState> = listOf(),
   instructionsUiState: List<InstructionUiState> = listOf(),
   updateRecipeUiState: (RecipeUiState) -> Unit = {},
   updateIngredientUiState: (Int, String) -> Unit = { _, _ -> },
   updateInstructionUiState: (Int, String) -> Unit = { _, _ -> },
   removeIngredientUiState: (Int) -> Unit = {},
   addIngredientUiState: () -> Unit = {},
   removeInstructionUiState: (Int) -> Unit = {},
   addInstructionUiState: () -> Unit = {},
   saveRecipe: () -> Unit = {},
   navigateUp: () -> Unit = {},
   navigateBack: () -> Unit = {},
) {

   val snackBarHostState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()

   recipeUiState.successMessage?.let {
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

   recipeUiState.error?.let {
      scope.launch {
         snackBarHostState.showSnackbar(
            message = it,
            withDismissAction = true,
            duration = SnackbarDuration.Short
         )
      }
   }

   Scaffold(
      snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
      modifier = modifier,
      topBar = {
         RecipeEntryToolBar(
            modifier = modifier,
            navigateUp = navigateUp,
            saveRecipe = saveRecipe
         )
      }
   ) {

      Column(
         modifier = modifier
            .fillMaxSize()
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

         val modifier = Modifier
            .fillMaxWidth()
            .padding(
               start = dimensionResource(R.dimen.padding_small),
               end = dimensionResource(R.dimen.padding_small),
               top = dimensionResource(R.dimen.padding_small)
            )

         //Nhap ten mon
         RecipeEntryInput(
            label = R.string.recipe_entry_name_label,
            placeHolder = R.string.recipe_entry_name_placeholder,
            modifier = modifier,
            value = recipeUiState.title,
            onValueChange = {
               updateRecipeUiState(recipeUiState.copy(title = it))
            },
            keyboardOptions = KeyboardOptions(
               capitalization = KeyboardCapitalization.Sentences,
               keyboardType = KeyboardType.Text,
               imeAction = ImeAction.Next,
            )
         )

         //Nhap thong tin
         RecipeEntryInput(
            label = R.string.recipe_entry_info_label,
            placeHolder = R.string.recipe_entry_info_placeholder,
            modifier = modifier.height(150.dp),
            value = recipeUiState.brief,
            onValueChange = { brief ->
               updateRecipeUiState(recipeUiState.copy(brief = brief))
            },
            keyboardOptions = KeyboardOptions(
               capitalization = KeyboardCapitalization.Sentences,
               keyboardType = KeyboardType.Text,
               imeAction = ImeAction.Next,
            )
         )

         //nhap khau phan
         Row(
            modifier,
            verticalAlignment = Alignment.CenterVertically
         ) {
            Text(stringResource(R.string.recipe_entry_serving_labelTruoc))

            Spacer(modifier = Modifier.weight(1f))

            RecipeEntryInput(
               modifier = Modifier.width(100.dp),
               label = R.string.recipe_entry_serving_labelSau,
               placeHolder = R.string.default00,
               value = recipeUiState.servings,
               onValueChange = {
                  updateRecipeUiState(recipeUiState.copy(servings = it))
               },
               keyboardOptions = KeyboardOptions(
                  keyboardType = KeyboardType.Number,
                  imeAction = ImeAction.Next,
               )
            )
         }

         //nhap thoi gian nau
         Row(
            modifier,
            verticalAlignment = Alignment.CenterVertically
         ) {
            Text(stringResource(R.string.recipe_entry_cook_time_label))

            Spacer(modifier = Modifier.weight(1f))

            RecipeEntryInput(
               modifier = Modifier.width(100.dp),
               label = R.string.hour_label,
               placeHolder = R.string.default00,
               value = recipeUiState.cookTimeHours,
               onValueChange = {
                  updateRecipeUiState(recipeUiState.copy(cookTimeHours = it))
               },
               keyboardOptions = KeyboardOptions(
                  keyboardType = KeyboardType.Number,
                  imeAction = ImeAction.Next,
               )
            )

            RecipeEntryInput(
               modifier = Modifier.width(100.dp),
               label = R.string.minute_label,
               placeHolder = R.string.default00,
               value = recipeUiState.cookTimeMinutes,
               onValueChange = {
                  updateRecipeUiState(recipeUiState.copy(cookTimeMinutes = it))
               },
               keyboardOptions = KeyboardOptions(
                  keyboardType = KeyboardType.Number,
                  imeAction = ImeAction.Next,
               )
            )
         }

         //nhap nguyen lieu
         Column(modifier) {
            Text(stringResource(R.string.nguyen_lieu))

            ingredientsUiState.forEach { ingredientUiState ->
               Row(
                  modifier,
                  verticalAlignment = Alignment.CenterVertically
               ) {
                  OutlinedTextField(
                     placeholder = { Text(stringResource(R.string.ingredient_entry_place_holder)) },
                     value = ingredientUiState.name,
                     onValueChange = {
                        updateIngredientUiState(ingredientUiState.id, it)
                     },
                     modifier = Modifier.weight(1f),
                     singleLine = true,
                  )

                  IconButton(onClick = { removeIngredientUiState(ingredientUiState.id) }) {
                     Icon(Icons.Default.Delete, contentDescription = null)
                  }
               }
            }

            OutlinedButton(
               onClick = { addIngredientUiState() },
            ) {
               Icon(imageVector = Icons.Default.Add, contentDescription = null)
               Text(stringResource(R.string.them_nguyen_lieu))
            }
         }

         //nhap buoc lam
         Column(
            modifier
         ) {
            Text(stringResource(R.string.cach_lam))

            instructionsUiState.forEach { instructionUiState ->
               Row(
                  modifier,
                  verticalAlignment = Alignment.CenterVertically
               ) {
                  CircularText("${instructionUiState.id}")
                  Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
                  OutlinedTextField(
                     value = instructionUiState.name,
                     onValueChange = {
                        updateInstructionUiState(instructionUiState.id, it)
                     },
                     modifier = Modifier.weight(1f),
                     singleLine = true,
                  )

                  IconButton(onClick = { removeInstructionUiState(instructionUiState.id) }) {
                     Icon(Icons.Default.Delete, contentDescription = null)
                  }
               }
            }

            OutlinedButton(
               onClick = { addInstructionUiState() },
            ) {
               Icon(imageVector = Icons.Default.Add, contentDescription = null)
               Text(stringResource(R.string.them_buoc_lam))
            }

            Spacer(Modifier.height(64.dp))
         }
      }
   }
}

//@Preview
@Composable
fun RecipeEntryInput(
   modifier: Modifier = Modifier,
   @StringRes label: Int = R.string.recipe_entry_name_label,
   @StringRes placeHolder: Int = R.string.recipe_entry_name_placeholder,
   value: String = "",
   onValueChange: (String) -> Unit = {},
   keyboardOptions: KeyboardOptions = KeyboardOptions(),
) {
   OutlinedTextField(
      value = value,
      onValueChange = {
         onValueChange(it)
      },
      label = { Text(stringResource(label)) },
      placeholder = { Text(stringResource(placeHolder)) },
      modifier = modifier,
      keyboardOptions = keyboardOptions
   )
}

@Preview
@Composable
fun CircularText(text: String = "1") {
   Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
         .size(32.dp) // Kích thước vuông
         .clip(CircleShape) // Bo thành hình tròn
         .background(Color.DarkGray) // Màu nền
   ) {
      Text(
         text = text,
         color = Color.White
      )
   }
}
