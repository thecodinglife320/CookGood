package com.ad.cookgood.myrecipes.presentation.state

data class IngredientUiState(
   override val id: Int = 0,
   override val name: String = "",
) : CommonUiState