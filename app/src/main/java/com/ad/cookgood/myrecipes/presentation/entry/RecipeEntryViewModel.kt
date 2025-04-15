package com.ad.cookgood.myrecipes.presentation.entry

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.myrecipes.domain.usecase.AddIngredientUseCase
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
) : ViewModel() {

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

   fun saveRecipe() {
      viewModelScope.launch(coroutineExceptionHandler) {
         val recipeId = addRecipeUseCase(_recipeUiState.value.toDomain())

         _ingredientsUiState.value.forEach {
            addIngredientUseCase(it.toDomain(), recipeId)
         }

         _recipeUiState.value = _recipeUiState.value.copy(
            addedRecipeId = recipeId,
            successMessage = "da luu"
         )
      }
   }
}