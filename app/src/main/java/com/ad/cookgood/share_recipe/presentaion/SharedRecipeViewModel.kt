package com.ad.cookgood.share_recipe.presentaion

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.like_recipe.domain.model.FavoriteRecipe
import com.ad.cookgood.like_recipe.domain.usecase.AddFavoriteUseCase
import com.ad.cookgood.navigation.data.SharedRecipeDetailScreen
import com.ad.cookgood.profile.domain.GetCurrentUserUseCase
import com.ad.cookgood.share_recipe.domain.model.SharedRecipeDetails
import com.ad.cookgood.share_recipe.domain.usecase.GetSharedRecipeDetailUseCase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedRecipeViewModel @Inject constructor(
   private val getSharedRecipeDetailUseCase: GetSharedRecipeDetailUseCase,
   private val addFavoriteUseCase: AddFavoriteUseCase,
   private val getCurrentUserUseCase: GetCurrentUserUseCase,
   stateHandle: SavedStateHandle
) : ViewModel() {

   private val sharedRecipeId: String =
      checkNotNull(stateHandle[SharedRecipeDetailScreen.sharedRecipeIdArg])
   private var currentUser: FirebaseUser? = null

   private val _sharedRecipeDetails = mutableStateOf<SharedRecipeDetails?>(null)
   val sharedRecipeDetails: State<SharedRecipeDetails?> = _sharedRecipeDetails

   init {
      viewModelScope.launch {
         currentUser = getCurrentUserUseCase().first()
         _sharedRecipeDetails.value = getSharedRecipeDetail()
      }
   }

   init {
      Log.d(TAG, sharedRecipeId)
   }

   suspend fun getSharedRecipeDetail() = getSharedRecipeDetailUseCase(sharedRecipeId)

   fun addFavorite() =
      viewModelScope.launch {
         addFavoriteUseCase(
            FavoriteRecipe(
               recipeId = sharedRecipeId,
               userId = currentUser?.uid ?: ""
            )
         )
      }

   private companion object {
      const val TAG = "SharedRecipeViewModel"
   }

}