package com.ad.cookgood.mycookbook.presentaion.mycookbook

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.mycookbook.domain.model.toMyRecipeUiState
import com.ad.cookgood.mycookbook.domain.usecase.GetMyCookBookUseCase
import com.ad.cookgood.mycookbook.presentaion.state.MyCookBookUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCookBookViewModel @Inject constructor(
   private val getMyCookBookUseCase: GetMyCookBookUseCase,
) : ViewModel() {

   //prepare uiState
   private val _myCookBookUiState = mutableStateOf(
      MyCookBookUiState(
         myRecipeUiStates = listOf(),
         isLoading = true
      )
   )

   //expose uiState
   val myCookBookUiState: State<MyCookBookUiState> get() = _myCookBookUiState

   init {
      viewModelScope.launch {
         _myCookBookUiState.value = _myCookBookUiState.value.copy(
            myRecipeUiStates = getMyCookBookUseCase().myRecipes.map {
               it.toMyRecipeUiState()
            },
            isLoading = false
         )
         _myCookBookUiState.value.myRecipeUiStates
      }
   }
}