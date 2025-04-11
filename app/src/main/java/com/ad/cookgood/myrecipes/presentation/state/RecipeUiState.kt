package com.ad.cookgood.myrecipes.presentation.state

data class RecipeUiState(
   val title: String = "",
   val brief: String = "",
   val servings: String = "",
   val cookTimeMinutes: String = "",
   val cookTimeHours: String = "",
   val error: String? = null,
   val successMessage: String? = null,
   val addedRecipeId: Long? = null,
)