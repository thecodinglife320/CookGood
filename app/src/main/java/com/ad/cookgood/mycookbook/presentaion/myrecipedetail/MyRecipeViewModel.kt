package com.ad.cookgood.mycookbook.presentaion.myrecipedetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.mycookbook.domain.model.toMyRecipeUiState
import com.ad.cookgood.mycookbook.domain.usecase.GetIngredientsOfMyRecipeUseCase
import com.ad.cookgood.mycookbook.domain.usecase.GetInstructionsOfMyRecipeUseCase
import com.ad.cookgood.mycookbook.domain.usecase.GetMyRecipeUseCase
import com.ad.cookgood.mycookbook.presentaion.state.MyRecipeUiState
import com.ad.cookgood.navigation.data.MyRecipeDetailScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecipeViewModel @Inject constructor(
   getMyRecipeUseCase: GetMyRecipeUseCase,
   getInstructionsOfMyRecipeUseCase: GetInstructionsOfMyRecipeUseCase,
   getIngredientsOfMyRecipeUseCase: GetIngredientsOfMyRecipeUseCase,
   stateHandle: SavedStateHandle,
) : ViewModel() {

   //prepare state
   private val _myRecipeUiState = mutableStateOf(
      MyRecipeUiState()
   )

   //expose state
   val myRecipeDetailUiState: State<MyRecipeUiState> get() = _myRecipeUiState

   init {

      val id = stateHandle.get<Long>(MyRecipeDetailScreen.recipeIdArg) ?: 0

      viewModelScope.launch {
         getMyRecipeUseCase(id)?.let {
            _myRecipeUiState.value = it.toMyRecipeUiState()
         }
      }
   }

}