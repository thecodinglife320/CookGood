package com.ad.cookgood.mycookbook.presentaion.state

import com.ad.cookgood.share_recipe.domain.model.SharedRecipe

sealed interface SharedMyRecipeGridUiState {
   object Loading : SharedMyRecipeGridUiState
   data class Error(val message: String) : SharedMyRecipeGridUiState
   data class Success(
      val sharedMyRecipeUiStates: List<SharedRecipe>
   ) : SharedMyRecipeGridUiState
}