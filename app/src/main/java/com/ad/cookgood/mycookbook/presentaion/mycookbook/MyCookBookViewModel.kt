package com.ad.cookgood.mycookbook.presentaion.mycookbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.mycookbook.domain.model.toMyRecipeUiState
import com.ad.cookgood.mycookbook.domain.usecase.GetMyRecipesUseCase
import com.ad.cookgood.mycookbook.presentaion.state.MyCookBookUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyCookBookViewModel @Inject constructor(
   private val useCase: GetMyRecipesUseCase,
) : ViewModel() {

   //prepare uiState
   val myCookBookUiState =
      useCase()
         .map {
            MyCookBookUiState(it.map {
               it.toMyRecipeUiState()
            })
         }
         .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = MyCookBookUiState(listOf())
         )

   private companion object {
      private const val TIMEOUT_MILLIS = 5_000L
   }
}