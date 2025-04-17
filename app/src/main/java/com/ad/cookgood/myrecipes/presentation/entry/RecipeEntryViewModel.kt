package com.ad.cookgood.myrecipes.presentation.entry

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.myrecipes.domain.usecase.AddIngredientUseCase
import com.ad.cookgood.myrecipes.domain.usecase.AddInstructionUseCase
import com.ad.cookgood.myrecipes.domain.usecase.AddRecipeUseCase
import com.ad.cookgood.myrecipes.presentation.state.IngredientUiState
import com.ad.cookgood.myrecipes.presentation.state.InstructionUiState
import com.ad.cookgood.myrecipes.presentation.state.RecipeUiState
import com.ad.cookgood.myrecipes.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeEntryViewModel @Inject constructor(
   private val addRecipeUseCase: AddRecipeUseCase,
   private val addIngredientUseCase: AddIngredientUseCase,
   private val addInstructionUseCase: AddInstructionUseCase,
) : ViewModel() {

   //prepare state
   private val _recipeUiState = mutableStateOf(
      RecipeUiState()
   )

   private val _ingredientUiStates = mutableStateOf(
      listOf<IngredientUiState>()
   )

   private val _instructionUiStates = mutableStateOf(
      listOf<InstructionUiState>()
   )

   //expose state
   val recipeUiState: State<RecipeUiState> get() = _recipeUiState

   val ingredientUiStates: State<List<IngredientUiState>> get() = _ingredientUiStates

   val instructionUiStates: State<List<InstructionUiState>> get() = _instructionUiStates

   //coroutine exception handle
   private val coroutineExceptionHandler =
      CoroutineExceptionHandler { _, ex ->
         ex.printStackTrace()
         _recipeUiState.value = _recipeUiState.value.copy(
            error = ex.message
         )
      }

   fun updateRecipeUiState(recipeUiState: RecipeUiState) =
      let {
         _recipeUiState.value = recipeUiState
      }

   fun addIngredientUiState() {
      val newId = (_ingredientUiStates.value.maxOfOrNull { it.id } ?: 0) + 1
      _ingredientUiStates.value = _ingredientUiStates.value + IngredientUiState(id = newId)
   }

   fun updateIngredientUiState(id: Int, newName: String) {
      _ingredientUiStates.value = _ingredientUiStates.value.map {
         if (it.id == id) it.copy(name = newName) else it
      }
   }

   fun removeIngredientUiState(id: Int) {
      _ingredientUiStates.value = _ingredientUiStates.value.filterNot { it.id == id }
   }

   fun updateInstructionUiState(id: Int, newName: String) {
      _instructionUiStates.value = _instructionUiStates.value.map {
         if (it.id == id) it.copy(name = newName) else it
      }
   }

   fun addInstructionUiState() {
      val newId = (_instructionUiStates.value.maxOfOrNull { it.id } ?: 0) + 1
      _instructionUiStates.value = _instructionUiStates.value + InstructionUiState(id = newId)
   }

   fun removeInstructionUiState(id: Int) {
      _instructionUiStates.value = _instructionUiStates.value
         .filterNot { it.id == id }
         .mapIndexed { index, instructionUiState -> instructionUiState.copy(id = index + 1) }
   }

   fun saveRecipe() {
      viewModelScope.launch(coroutineExceptionHandler) {
         val recipeId = addRecipeUseCase(_recipeUiState.value.toDomain())

         _ingredientUiStates.value.forEach {
            addIngredientUseCase(it.toDomain(), recipeId)
         }

         _instructionUiStates.value.forEach {
            addInstructionUseCase(it.toDomain(it.id), recipeId = recipeId)
         }

         _recipeUiState.value = _recipeUiState.value.copy(
            addedRecipeId = recipeId,
            successMessage = "da luu"
         )
      }
   }
}