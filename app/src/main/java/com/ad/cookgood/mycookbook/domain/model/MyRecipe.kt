package com.ad.cookgood.mycookbook.domain.model

import com.ad.cookgood.mycookbook.presentaion.state.MyRecipeUiState
import com.ad.cookgood.recipes.domain.model.Recipe
import com.ad.cookgood.recipes.domain.model.toRecipeUiState

data class MyRecipe(
   val id: Long,
   val recipe: Recipe,
)

fun MyRecipe.toMyRecipeUiState() =
   MyRecipeUiState(
      id = id,
      recipeUiState = recipe.toRecipeUiState(),
   )