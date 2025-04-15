package com.ad.cookgood.myrecipes.presentation.entry

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.ad.cookgood.myrecipes.domain.usecase.AddIngredientUseCase
import com.ad.cookgood.myrecipes.domain.usecase.AddRecipeUseCase
import com.ad.cookgood.myrecipes.presentation.state.IngredientUiState
import com.ad.cookgood.myrecipes.presentation.state.InstructionUiState
import com.ad.cookgood.myrecipes.presentation.state.RecipeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
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

   private val _ingredientUiStates = mutableStateListOf(
      IngredientUiState()
   )

   private val _instructionUiStates = mutableStateListOf(
      InstructionUiState()
   )

   //expose state
   val recipeUiState: State<RecipeUiState> get() = _recipeUiState

   val ingredientsUiState: List<IngredientUiState> get() = _ingredientUiStates

   val instructionsUiState: List<InstructionUiState> get() = _instructionUiStates

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
      _ingredientUiStates.add(IngredientUiState())
   }

   fun updateIngredientUiState(uiState: IngredientUiState, newName: String) {
      _ingredientUiStates[_ingredientUiStates.indexOf(uiState)] = uiState.copy(
         name = newName
      )
   }

   fun removeIngredientUiState(uiState: IngredientUiState) {
      _ingredientUiStates.remove(uiState)
   }

   fun updateInstructionUiState(uiState: InstructionUiState, newName: String) {
      _instructionUiStates[_instructionUiStates.indexOf(uiState)] = uiState.copy(
         name = newName
      )
   }

   fun addInstructionUiState() {
      _instructionUiStates.add(InstructionUiState())
   }

   fun removeInstructionUiState(uiState: InstructionUiState) {
      _instructionUiStates.remove(uiState)
   }

   fun saveRecipe() {
//      viewModelScope.launch(coroutineExceptionHandler) {
//         val recipeId = addRecipeUseCase(_recipeUiState.value.toDomain())
//
//         _ingredientUiStates.value.forEach {
//            addIngredientUseCase(it.toDomain(), recipeId)
//         }
//
//         _recipeUiState.value = _recipeUiState.value.copy(
//            addedRecipeId = recipeId,
//            successMessage = "da luu"
//         )
//      }
   }
}