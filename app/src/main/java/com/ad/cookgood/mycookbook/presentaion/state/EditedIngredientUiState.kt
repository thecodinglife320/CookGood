package com.ad.cookgood.mycookbook.presentaion.state

import com.ad.cookgood.mycookbook.domain.model.EditedIngredient
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.toDomain

data class EditedIngredientUiState(
   val ingredientUiState: IngredientUiState,
   val id: Long,
   val recipeId: Long
)

fun EditedIngredientUiState.toDomain() =
   EditedIngredient(
      id = id,
      ingredient = ingredientUiState.toDomain(),
      recipeId = recipeId
   )