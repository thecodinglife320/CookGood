package com.ad.cookgood.myrecipes.presentation.entry

data class RecipeEntryUiState(
   val name: String = "",
   val brief: String = "",
   val servings: String = "",
   val cookTimeMinutes: String = "",
   val cookTimeHours: String = "",
)