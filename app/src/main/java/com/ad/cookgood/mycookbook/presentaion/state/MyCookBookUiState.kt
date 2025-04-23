package com.ad.cookgood.mycookbook.presentaion.state

data class MyCookBookUiState(
   val myRecipeUiStates: List<MyRecipeUiState>,
   val isLoading: Boolean,
)
