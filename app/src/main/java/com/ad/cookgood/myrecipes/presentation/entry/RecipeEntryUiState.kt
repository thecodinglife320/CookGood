package com.ad.cookgood.myrecipes.presentation.entry

data class RecipeEntryUiState(
   val name: String = "",
   val brief: String = "",
   val serving: Int = 0,
   val cookTime: Int = 0,
)
