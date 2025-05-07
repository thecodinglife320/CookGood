package com.ad.cookgood.mycookbook.presentaion.state

import com.ad.cookgood.mycookbook.domain.model.MyRecipe
import com.ad.cookgood.recipes.presentation.state.RecipeUiState
import com.ad.cookgood.recipes.presentation.state.toDomain

data class MyRecipeUiState(
   val id: Long = 0,
   val recipeUiState: RecipeUiState = RecipeUiState()
)

fun MyRecipeUiState.toDomain() =
   MyRecipe(
      id = id,
      recipe = recipeUiState.toDomain()
   )