package com.ad.cookgood.recipes.domain.model

import androidx.core.net.toUri
import com.ad.cookgood.recipes.data.local.recipe.LocalRecipe
import com.ad.cookgood.recipes.presentation.state.RecipeUiState

data class Recipe(
   val title: String = "",
   val brief: String = "",
   val serving: Int = 0,
   val cookTime: Int = 0,
   val uri: String? = null
)

fun Recipe.toLocal() =
   LocalRecipe(
      title = title,
      brief = brief,
      servings = serving,
      cookTime = cookTime,
      uri = uri?.toUri()
   )

fun Recipe.toRecipeUiState() =
   RecipeUiState(
      title = title,
      brief = brief,
      servings = "$serving",
      cookTimeMinutes = "${cookTime % 60}",
      cookTimeHours = "${cookTime / 60}",
      uri = uri?.toUri()
   )
