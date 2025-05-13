package com.ad.cookgood.mycookbook.domain.model

import com.ad.cookgood.mycookbook.presentaion.state.MyRecipeUiState
import com.ad.cookgood.recipes.data.local.recipe.LocalRecipe
import com.ad.cookgood.recipes.domain.model.Recipe
import com.ad.cookgood.recipes.presentation.state.RecipeUiState

data class MyRecipe(
   val id: Long,
   val recipe: Recipe,
)

fun MyRecipe.toMyRecipeUiState() =
   MyRecipeUiState(
      id = id,
      recipeUiState = RecipeUiState(
         title = recipe.title,
         brief = recipe.brief,
         servings = "${recipe.serving} người",
         cookTimeMinutes = "${recipe.cookTime % 60} phút",
         cookTimeHours = "${recipe.cookTime / 60} tiếng",
         uri = recipe.uri
      ),
   )

fun MyRecipe.toLocal() =
   LocalRecipe(
      id = id,
      title = recipe.title,
      brief = recipe.brief,
      servings = recipe.serving,
      cookTime = recipe.cookTime,
      uri = recipe.uri
   )