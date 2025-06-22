package com.ad.cookgood.mycookbook.presentaion.mycookbook

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.mycookbook.domain.usecase.GetMyRecipesUseCase
import com.ad.cookgood.mycookbook.domain.usecase.GetSharedMyRecipesUseCase
import com.ad.cookgood.mycookbook.presentaion.state.MyRecipeUiState
import com.ad.cookgood.mycookbook.presentaion.state.SharedMyRecipeGridUiState
import com.ad.cookgood.mycookbook.presentaion.state.SharedMyRecipeGridUiState.Loading
import com.ad.cookgood.profile.domain.GetCurrentUserUseCase
import com.ad.cookgood.recipes.domain.model.toRecipeUiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCookBookViewModel @Inject constructor(
   getMyRecipesUseCase: GetMyRecipesUseCase,
   getSharedMyRecipesUseCase: GetSharedMyRecipesUseCase,
   private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {

   private val _currentUser = mutableStateOf<FirebaseUser?>(null)
   private val _sharedMyRecipeGridUiState = MutableStateFlow<SharedMyRecipeGridUiState>(Loading)
   val sharedMyRecipeGridUiState = _sharedMyRecipeGridUiState.asStateFlow()

   init {
      viewModelScope.launch {
         getCurrentUserUseCase().collect {
            _currentUser.value = it
            getSharedMyRecipesUseCase(_currentUser.value?.uid ?: "")
               .onStart {
                  _sharedMyRecipeGridUiState.value = Loading
               }
               .catch {
               }
               .collect {
                  _sharedMyRecipeGridUiState.value = SharedMyRecipeGridUiState.Success(it)
               }
         }
      }
   }

   //prepare uiState
   val myRecipeUiStates =
      getMyRecipesUseCase()
         .map {
            it.map {
               MyRecipeUiState(it.id, it.recipe.toRecipeUiState())
            }
         }
         .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = listOf()
         )

   private companion object {
      private const val TIMEOUT_MILLIS = 5_000L
   }
}