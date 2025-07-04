package com.ad.cookgood.mycookbook.presentaion.myrecipedetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Share
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.cookgood.R
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.InstructionUiState
import com.ad.cookgood.recipes.presentation.state.RecipeUiState
import com.ad.cookgood.shared.CoilImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRecipeDetailScreen(
   navigateUp: () -> Unit,
   navigateBack: () -> Unit,
   navigateToEditScreen: (Long) -> Unit
) {
   val vm: MyRecipeViewModel = hiltViewModel()
   val myRecipeUiState by vm.myRecipeUiState.collectAsState()
   val instructionUiStates by vm.instructionUiStates.collectAsState()
   val ingredientUiStates by vm.ingredientUiStates.collectAsState()
   val currentUser by vm.currentUser.collectAsState()
   var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
   val shareConfirmDialogUiState by vm.shareConfirmDialogUiState.collectAsState()

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

   if (shareConfirmDialogUiState.isVisible) {
      ShareConfirmDialog(
         onDismiss = { vm.onShareConfirmDialogDismiss() },
         onShare = {
            vm.onShareConfirmDialogConfirm()
         },
         isSharing = shareConfirmDialogUiState.isSharing,
      )
   }

   myRecipeUiState?.let { myRecipeUiState ->
      Scaffold(
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
                  currentUser?.let {
                     if (it.isAnonymous.not()) {
                        IconButton(onClick = {
                           vm.onLaunchShareConfirmDialog()
                        }) {
                           Icon(
                              Icons.Rounded.Share,
                              contentDescription = null
                           )
                        }
                     }
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
         MyRecipeDetailScreenContent(
            modifier = Modifier
               .padding(it)
               .padding(horizontal = dimensionResource(R.dimen.padding_medium))
               .verticalScroll(rememberScrollState()),
            recipeUiState = myRecipeUiState.recipeUiState,
            ingredientUiStates = ingredientUiStates,
            instructionUiStates = instructionUiStates
         )
      }
   } ?: Box(Modifier.fillMaxWidth()) { Text("loi") }
}

@Preview
@Composable
fun MyRecipeDetailScreenContent(
   modifier: Modifier = Modifier,
   recipeUiState: RecipeUiState = RecipeUiState(),
   ingredientUiStates: List<IngredientUiState> = listOf(IngredientUiState(1, "rau cai")),
   instructionUiStates: List<InstructionUiState> = listOf(
      InstructionUiState(name = "alo", stepNumber = 1, uri = null),
      InstructionUiState(name = "alo", stepNumber = 2, uri = null),
      InstructionUiState(name = "alo", stepNumber = 3, uri = null)
   )
) {
   Column(modifier) {

      CoilImage(
         uri = recipeUiState.uri,
         modifier = Modifier.height(300.dp)
      )

      Spacer(Modifier.size(dimensionResource(R.dimen.padding_medium)))

      //recipe info
      RecipeInfoCard(
         cookTimeMinutes = recipeUiState.cookTimeMinutes,
         cookTimeHours = recipeUiState.cookTimeHours,
         servings = recipeUiState.servings,
         brief = recipeUiState.brief
      )

      //nguyen lieu
      Text(
         stringResource(R.string.nguyen_lieu)
      )
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