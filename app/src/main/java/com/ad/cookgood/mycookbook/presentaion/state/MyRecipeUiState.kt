package com.ad.cookgood.mycookbook.presentaion.state

import com.ad.cookgood.recipes.presentation.state.RecipeUiState

data class MyRecipeUiState(
   val id: Long = 0,
   val recipeUiState: RecipeUiState = RecipeUiState()
)