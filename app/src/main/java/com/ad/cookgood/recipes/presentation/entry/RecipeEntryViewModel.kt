package com.ad.cookgood.recipes.presentation.entry

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.recipes.domain.usecase.AddIngredientUseCase
import com.ad.cookgood.recipes.domain.usecase.AddInstructionUseCase
import com.ad.cookgood.recipes.domain.usecase.AddRecipeUseCase
import com.ad.cookgood.recipes.presentation.state.CommonUiState
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.InstructionUiState
import com.ad.cookgood.recipes.presentation.state.RecipeUiState
import com.ad.cookgood.recipes.presentation.state.toDomain
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

   private val _successMessage: MutableState<String?> = mutableStateOf(
      null
   )

   private val _error: MutableState<String?> = mutableStateOf(
      null
   )

   //expose state
   val ingredientUiStates: State<List<IngredientUiState>> get() = _ingredientUiStates

   val instructionUiStates: State<List<InstructionUiState>> get() = _instructionUiStates

   val successMessage: State<String?> = _successMessage

   val error: State<String?> = _error

   //coroutine exception handle
   private val coroutineExceptionHandler =
      CoroutineExceptionHandler { _, ex ->
         _error.value = ex.message
      }

   fun addCommonUiState(uiState: CommonUiState) {
      when (uiState) {
         is IngredientUiState -> _ingredientUiStates.value = _ingredientUiStates.value + uiState

         is InstructionUiState -> {
            var stepNumber = (_instructionUiStates.value.maxOfOrNull { it.stepNumber } ?: 0) + 1
            _instructionUiStates.value = _instructionUiStates.value + uiState.apply {
               this.stepNumber = stepNumber
            }
         }
      }
   }

   fun updateCommonUiState(uiState: CommonUiState, newName: String) {
      when (uiState) {
         is IngredientUiState -> _ingredientUiStates.value = _ingredientUiStates.value.map {
            if (it.id == uiState.id) it.copy(name = newName) else it
         }

         is InstructionUiState -> _instructionUiStates.value = _instructionUiStates.value.map {
            if (it.id == uiState.id) it.copy(name = newName) else it
         }
      }
   }

   fun removeCommonUiState(uiState: CommonUiState) {
      when (uiState) {
         is InstructionUiState -> {
            _instructionUiStates.value =
               (_instructionUiStates.value - uiState).mapIndexed { index, uiState ->
                  uiState.apply { stepNumber = index + 1 }
               }
         }

         is IngredientUiState -> _ingredientUiStates.value = _ingredientUiStates.value - uiState
      }
   }

   fun saveRecipe() {
      viewModelScope.launch(coroutineExceptionHandler) {
         val recipeId = addRecipeUseCase(_recipeUiState.value.toDomain())

         _ingredientUiStates.value.forEach {
            addIngredientUseCase(it.toDomain(), recipeId)
         }

         _instructionUiStates.value.forEach {
            addInstructionUseCase(it.toDomain(), recipeId = recipeId)
         }

         _successMessage.value = "da luu"
      }
   }

   fun onTitleChange(title: String) {
      _recipeUiState.value = _recipeUiState.value.copy(
         title = title
      )
   }

   fun onBriefChange(brief: String) {
      _recipeUiState.value = _recipeUiState.value.copy(
         brief = brief
      )
   }

   fun onServingChange(serving: String) {
      _recipeUiState.value = _recipeUiState.value.copy(
         servings = serving
      )
   }

   fun onHourChange(hour: String) {
      _recipeUiState.value = _recipeUiState.value.copy(
         cookTimeHours = hour
      )
   }

   fun onMinuteChange(minute: String) {
      _recipeUiState.value = _recipeUiState.value.copy(
         cookTimeMinutes = minute
      )
   }

}