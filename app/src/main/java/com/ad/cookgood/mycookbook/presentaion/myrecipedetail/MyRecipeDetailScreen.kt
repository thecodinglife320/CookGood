package com.ad.cookgood.mycookbook.presentaion.myrecipedetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ad.cookgood.R
import com.ad.cookgood.shared.CoilImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRecipeDetailScreen(
   modifier: Modifier = Modifier,
   vm: MyRecipeViewModel,
   navigateUp: () -> Unit,
   navigateBack: () -> Unit,
   navigateToEditScreen: (Long) -> Unit
) {

   val myRecipeUiState by vm.myRecipeUiState.collectAsState()
   val instructionUiStates by vm.instructionUiStates.collectAsState()
   val ingredientUiStates by vm.ingredientUiStates.collectAsState()
   var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

   if (deleteConfirmationRequired) {
      DeleteConfirmationDialog(
         onDeleteConfirm = {
            deleteConfirmationRequired = false
            vm.deleteMyRecipe()
            navigateBack()
         },
         onDeleteCancel = { deleteConfirmationRequired = false },
         modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
      )
   }

   myRecipeUiState?.let { myRecipeUiState ->
      Scaffold(
         modifier = modifier,
         topBar = {
            TopAppBar(
               title = { Text(myRecipeUiState.recipeUiState.title) },
               actions = {
                  IconButton(onClick = { deleteConfirmationRequired = true }) {
                     Icon(
                        Icons.Rounded.Delete,
                        contentDescription = null
                     )
                  }
                  IconButton(onClick = {
                     navigateToEditScreen(myRecipeUiState.id)
                  }) {
                     Icon(
                        Icons.Rounded.Edit,
                        contentDescription = null
                     )
                  }
               },
               navigationIcon = {
                  IconButton(
                     onClick = navigateUp
                  ) {
                     Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back"
                     )
                  }
               },
            )
         },

         ) {
         Column(
            Modifier
               .padding(it)
               .verticalScroll(rememberScrollState())
         ) {

            //recipe avatar
            CoilImage(
               uri = myRecipeUiState.recipeUiState.uri,
               modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.size(dimensionResource(R.dimen.padding_medium)))

            //recipe info
            RecipeInfoCard(
               cookTimeMinutes = myRecipeUiState.recipeUiState.cookTimeMinutes,
               cookTimeHours = myRecipeUiState.recipeUiState.cookTimeHours,
               servings = myRecipeUiState.recipeUiState.servings,
               brief = myRecipeUiState.recipeUiState.brief
            )

            //nguyen lieu
            Text(stringResource(R.string.nguyen_lieu))
            IngredientColumn(
               ingredientUiStates = ingredientUiStates,
            )

            //buoc lam
            Text(stringResource(R.string.cach_lam))
            InstructionRow(
               instructionUiStates = instructionUiStates
            )
         }
      }
   } ?: Box(Modifier.fillMaxWidth()) { Text("loi") }
}

@Preview
@Composable
private fun DeleteConfirmationDialog(
   modifier: Modifier = Modifier,
   onDeleteConfirm: () -> Unit = {},
   onDeleteCancel: () -> Unit = {},
) {
   AlertDialog(
      onDismissRequest = { /* Do nothing */ },
      title = { Text(stringResource(R.string.attention)) },
      text = { Text(stringResource(R.string.delete_question)) },
      modifier = modifier,
      dismissButton = {
         TextButton(onClick = onDeleteCancel) {
            Text(stringResource(R.string.no))
         }
      },
      confirmButton = {
         TextButton(onClick = onDeleteConfirm) {
            Text(stringResource(R.string.yes))
         }
      })
}