package com.ad.cookgood.myrecipes.presentation.entry

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ad.cookgood.myrecipes.presentation.state.IngredientUiState
import com.ad.cookgood.myrecipes.presentation.state.InstructionUiState
import com.ad.cookgood.myrecipes.presentation.state.RecipeUiState

class RecipeEntryViewModel : ViewModel() {

   //prepare state
   private val _recipeUiState = mutableStateOf(
      RecipeUiState()
   )

   private val _ingredientsUiState = mutableStateOf(
      listOf<IngredientUiState>()
   )

   private val _instructionsUiState = mutableStateOf(
      listOf<InstructionUiState>()
   )

   //expose state
   val recipeUiState: State<RecipeUiState> get() = _recipeUiState

   val ingredientsUiState: State<List<IngredientUiState>> get() = _ingredientsUiState

   val instructionsUiState: State<List<InstructionUiState>> get() = _instructionsUiState

   fun updateRecipeUiState(recipeUiState: RecipeUiState) =
      let {
         _recipeUiState.value = recipeUiState
      }

   fun updateIngredientUiState(id: Int, newName: String) {
      _ingredientsUiState.value = _ingredientsUiState.value.map {
         if (it.id == id) it.copy(name = newName) else it
      }
   }

   fun updateInstructionUiState(id: Int, newName: String) {
      _instructionsUiState.value = _instructionsUiState.value.map {
         if (it.id == id) it.copy(name = newName) else it
      }
   }

   fun addIngredientUiState() {
      val newId = (_ingredientsUiState.value.maxOfOrNull { it.id } ?: 0) + 1
      _ingredientsUiState.value = _ingredientsUiState.value + IngredientUiState(id = newId)
   }

   fun addInstructionUiState() {
      val newId = (_instructionsUiState.value.maxOfOrNull { it.id } ?: 0) + 1
      _instructionsUiState.value = _instructionsUiState.value + InstructionUiState(id = newId)
   }

   fun removeIngredientUiState(id: Int) {
      _ingredientsUiState.value = _ingredientsUiState.value.filterNot { it.id == id }
   }

   fun removeInstructionUiState(id: Int) {
      _instructionsUiState.value = _instructionsUiState.value
         .filterNot { it.id == id }
         .mapIndexed { index, instructionUiState -> instructionUiState.copy(id = index + 1) }
   }
}