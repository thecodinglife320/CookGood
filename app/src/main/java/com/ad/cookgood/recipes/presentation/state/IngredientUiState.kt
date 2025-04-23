package com.ad.cookgood.recipes.presentation.state

import com.ad.cookgood.recipes.domain.model.Ingredient

data class IngredientUiState(
   override val id: Int = 0,
   override val name: String = "",
) : CommonUiState

fun IngredientUiState.toDomain() =
   Ingredient(name = name)