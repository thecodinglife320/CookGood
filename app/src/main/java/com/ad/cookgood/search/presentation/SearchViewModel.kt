package com.ad.cookgood.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.mycookbook.presentaion.state.SharedMyRecipeGridUiState
import com.ad.cookgood.mycookbook.presentaion.state.SharedMyRecipeGridUiState.Loading
import com.ad.cookgood.share_recipe.domain.usecase.GetRecentSharedRecipesUseCase
import com.ad.cookgood.share_recipe.domain.usecase.SearchRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
   private val searchRecipeUseCase: SearchRecipeUseCase,
   private val getRecentSharedRecipesUseCase: GetRecentSharedRecipesUseCase
) : ViewModel() {

   private val _searchedSharedRecipes = MutableStateFlow<SharedMyRecipeGridUiState>(
      SharedMyRecipeGridUiState.Success(emptyList())
   )
   val searchedSharedRecipes = _searchedSharedRecipes.asStateFlow()

   private val _recentSharedRecipes = MutableStateFlow<SharedMyRecipeGridUiState>(Loading)
   val recentSharedRecipes = _recentSharedRecipes.asStateFlow()

   init {
      viewModelScope.launch {
         val result = getRecentSharedRecipesUseCase(10)
         _recentSharedRecipes.value = SharedMyRecipeGridUiState.Success(result)
      }
   }

   fun onSearch(searchQuery: String) {
      viewModelScope.launch {
         _searchedSharedRecipes.value = Loading
         val result = searchRecipeUseCase(searchQuery)
         _searchedSharedRecipes.value = SharedMyRecipeGridUiState.Success(result)
      }
   }

   companion object {
      const val TAG = "SearchViewModel"
   }
}