package com.ad.cookgood.mycookbook.presentaion.myrecipedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.mycookbook.domain.model.toMyRecipeUiState
import com.ad.cookgood.mycookbook.domain.usecase.DeleteMyRecipeUseCase
import com.ad.cookgood.mycookbook.domain.usecase.GetIngredientsOfMyRecipeUseCase
import com.ad.cookgood.mycookbook.domain.usecase.GetInstructionsOfMyRecipeUseCase
import com.ad.cookgood.mycookbook.domain.usecase.GetMyRecipeUseCase
import com.ad.cookgood.mycookbook.presentaion.state.MyRecipeUiState
import com.ad.cookgood.mycookbook.presentaion.state.toDomain
import com.ad.cookgood.navigation.data.MyRecipeDetailScreen
import com.ad.cookgood.recipes.domain.model.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecipeViewModel @Inject constructor(
   getMyRecipeUseCase: GetMyRecipeUseCase,
   getInstructionsOfMyRecipeUseCase: GetInstructionsOfMyRecipeUseCase,
   getIngredientsOfMyRecipeUseCase: GetIngredientsOfMyRecipeUseCase,
   private val deleteMyRecipeUseCase: DeleteMyRecipeUseCase,
   stateHandle: SavedStateHandle,
) : ViewModel() {

   private val id = stateHandle.get<Long>(MyRecipeDetailScreen.recipeIdArg) ?: 0

   //prepare state
   val myRecipeUiState =
      getMyRecipeUseCase(id)
         .map {
            it?.toMyRecipeUiState()
         }
         .stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), MyRecipeUiState())

   val instructionUiStates =
      getInstructionsOfMyRecipeUseCase(id)
         .map {
            it.map {
               it.toUiState()
            }
         }
         .stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), listOf())

   val ingredientUiStates =
      getIngredientsOfMyRecipeUseCase(id)
         .map {
            it.map {
               it.toUiState()
            }
         }
         .stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), listOf())

   private companion object {
      const val TIMEOUT_MILLIS = 5_000L
   }

   fun deleteMyRecipe() =
      viewModelScope.launch {
         deleteMyRecipeUseCase(myRecipeUiState.value!!.toDomain())
      }
}
