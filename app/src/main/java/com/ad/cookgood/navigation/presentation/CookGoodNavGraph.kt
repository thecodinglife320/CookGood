package com.ad.cookgood.navigation.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ad.cookgood.mycookbook.presentaion.MyCookBookScreen
import com.ad.cookgood.myrecipes.presentation.entry.RecipeEntryScreen
import com.ad.cookgood.myrecipes.presentation.entry.RecipeEntryViewModel
import com.ad.cookgood.navigation.data.MyCookBookScreen
import com.ad.cookgood.navigation.data.RecipeEntryScreen
import com.ad.cookgood.navigation.data.SearchScreen
import com.ad.cookgood.search.presentation.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookGoodNavHost(
   navController: NavHostController,
   paddingValues: PaddingValues,
) {
   NavHost(
      navController = navController,
      startDestination = SearchScreen.route,
      modifier = Modifier.padding(paddingValues)
   ) {

      //search screen
      composable(route = SearchScreen.route) {
         SearchScreen()
      }

      //my cook book screen
      composable(route = MyCookBookScreen.route) {
         MyCookBookScreen()
      }

      //recipe entry screen
      composable(route = RecipeEntryScreen.route) {
         val vm: RecipeEntryViewModel = hiltViewModel()
         RecipeEntryScreen(
            recipeUiState = vm.recipeUiState.value,
            ingredientsUiState = vm.ingredientsUiState.value,
            instructionsUiState = vm.instructionsUiState.value,
            updateRecipeUiState = { vm.updateRecipeUiState(it) },
            updateIngredientUiState = { id, name -> vm.updateIngredientUiState(id, name) },
            updateInstructionUiState = { id, name -> vm.updateInstructionUiState(id, name) },
            removeIngredientUiState = { vm.removeIngredientUiState(it) },
            addIngredientUiState = { vm.addIngredientUiState() },
            removeInstructionUiState = { vm.removeInstructionUiState(it) },
            addInstructionUiState = { vm.addInstructionUiState() },
            saveRecipe = { vm.saveRecipe() }
         )
      }
   }
}