package com.ad.cookgood.myrecipes.presentation.entry

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RecipeEntryViewModel : ViewModel() {

   //prepare state
   private val _recipeEntryUiState = mutableStateOf(
      RecipeEntryUiState(
         name = "",
         brief = "",
         serving = 0,
         cookTime = 0
      )
   )

   //expose state
   val recipeEntryUiState: State<RecipeEntryUiState> get() = _recipeEntryUiState

   fun updateUiState(recipeEntryUiState: RecipeEntryUiState) {
      _recipeEntryUiState.value = recipeEntryUiState
   }

}