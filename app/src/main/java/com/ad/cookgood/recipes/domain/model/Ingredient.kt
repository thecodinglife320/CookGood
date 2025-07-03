package com.ad.cookgood.recipes.domain.model

import com.ad.cookgood.recipes.data.local.ingredient.LocalIngredient
import com.ad.cookgood.recipes.presentation.state.IngredientUiState

data class Ingredient(
   val name: String = "",
)

fun Ingredient.toLocal(recipeId: Long) =
   LocalIngredient(
      name = name,
      recipeId = recipeId
   )

fun Ingredient.toUiState() =
   IngredientUiState(
      name = name
   )