package com.ad.cookgood.mycookbook.domain.model

import com.ad.cookgood.mycookbook.presentaion.state.EditedIngredientUiState
import com.ad.cookgood.recipes.data.local.ingredient.LocalIngredient
import com.ad.cookgood.recipes.domain.model.Ingredient
import com.ad.cookgood.recipes.domain.model.toUiState

data class EditedIngredient(
   val id: Long,
   val ingredient: Ingredient,
   val recipeId: Long
)

fun EditedIngredient.toLocal() =
   LocalIngredient(
      id = id,
      name = ingredient.name,
      recipeId = recipeId
   )

fun EditedIngredient.toUiState() =
   EditedIngredientUiState(
      ingredientUiState = ingredient.toUiState(),
      id = id,
      recipeId = id
   )