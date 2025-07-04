package com.ad.cookgood.share_recipe.presentaion

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.SnackbarResult.Dismissed
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.cookgood.R
import com.ad.cookgood.recipes.domain.model.toRecipeUiState
import com.ad.cookgood.recipes.domain.model.toUiState
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.InstructionUiState
import com.ad.cookgood.shared.CoilImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedRecipeDetailScreen(
   navigateUp: () -> Unit
) {

   val vm = hiltViewModel<SharedRecipeViewModel>()
   val sharedRecipeDetails by vm.sharedRecipeDetails
   val isFavorite by vm.isFavorite
   val snackBarUiState by vm.snackBarUiState
   val snackBarHostState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()

   if (snackBarUiState.showSnackBar) {
      SideEffect {
         scope.launch {
            val result = snackBarHostState.showSnackbar(
               message = snackBarUiState.message,
               withDismissAction = true,
            )
            when (result) {
               Dismissed -> if (snackBarUiState.isError) {
                  vm.hideSnackBar()
               }

               ActionPerformed -> ""
            }
         }
      }
   }

   sharedRecipeDetails?.let {
      val recipeUiState = it.sharedRecipe.recipe.toRecipeUiState()
      Scaffold(
         topBar = {
            TopAppBar(
               title = {
                  Text(it.sharedRecipe.recipe.title)
               },
               navigationIcon = {
                  IconButton(onClick = navigateUp) {
                     Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Back"
                     )
                  }
               },
               actions = {
                  IconButton(onClick = if (isFavorite) vm::removeFavorite else vm::addFavorite) {
                     Icon(
                        if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null
                     )
                  }
               },
            )
         },
         snackbarHost = { SnackbarHost(snackBarHostState) }
      ) { p ->
         SharedRecipeDetailScreenContent(
            modifier = Modifier.padding(p),
            cookTime = "${recipeUiState.cookTimeHours} ${recipeUiState.cookTimeMinutes} ",
            servings = recipeUiState.servings,
            description = recipeUiState.brief,
            instructionUiStates = it.sharedInstructions.map { e -> e.instruction.toUiState() },
            ingredientUiStates = it.sharedIngredients.map { e -> e.ingredient.toUiState() },
            uri = recipeUiState.uri
         )
      }
   }
}

@Composable
fun SharedRecipeDetailScreenContent(
   modifier: Modifier = Modifier,
   cookTime: String = "1 tieng",
   servings: String = "1 nguoi",
   description: String = "abc",
   instructionUiStates: List<InstructionUiState> = emptyList(),
   ingredientUiStates: List<IngredientUiState> = emptyList(),
   uri: Uri?
) {
   Column(modifier = modifier.verticalScroll(rememberScrollState())) {

      CoilImage(uri, Modifier.height(250.dp))

      BasicInfo(
         modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_small))
            .fillMaxWidth(),
         cookTime = cookTime,
         servings = servings
      )

      Description(
         description = description,
         modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
      )

      SecondaryTextTabs(
         instructionUiStates = instructionUiStates,
         ingredientUiStates = ingredientUiStates
      )
   }
}