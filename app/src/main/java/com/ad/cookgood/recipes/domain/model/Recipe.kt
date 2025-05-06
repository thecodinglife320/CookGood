package com.ad.cookgood.recipes.domain.model

import android.net.Uri
import com.ad.cookgood.recipes.data.local.recipe.LocalRecipe
import com.ad.cookgood.recipes.presentation.state.RecipeUiState

class Recipe(
   val title: String = "",
   val brief: String = "",
   val serving: Int = 0,
   val cookTime: Int = 0,
   val uri: Uri?
)

fun Recipe.toLocal() =
   LocalRecipe(
      title = title,
      brief = brief,
      servings = serving,
      cookTime = cookTime,
      uri = uri
   )

fun Recipe.toRecipeUiState() =
   RecipeUiState(
      title = title,
      brief = brief,
      servings = "$serving người",
      cookTimeMinutes = "${cookTime % 60} phút",
      cookTimeHours = "${cookTime / 60} tiếng",
      uri = uri
   )
