package com.ad.cookgood.mycookbook.presentaion.state

import com.ad.cookgood.share_recipe.data.FirebaseRecipe

sealed interface SharedMyRecipeGridUiState {
   object Loading : SharedMyRecipeGridUiState
   data class Error(val message: String) : SharedMyRecipeGridUiState
   data class Success(
      val sharedMyRecipeUiStates: List<FirebaseRecipe>
   ) : SharedMyRecipeGridUiState
}