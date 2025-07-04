package com.ad.cookgood.share_recipe.presentaion

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.like_recipe.domain.CheckRecipeFavoriteUseCase
import com.ad.cookgood.like_recipe.domain.model.FavoriteRecipe
import com.ad.cookgood.like_recipe.domain.usecase.AddFavoriteUseCase
import com.ad.cookgood.like_recipe.domain.usecase.RemoveFavoriteUseCase
import com.ad.cookgood.navigation.data.SharedRecipeDetailScreen
import com.ad.cookgood.profile.domain.GetCurrentUserUseCase
import com.ad.cookgood.share_recipe.domain.model.SharedRecipeDetails
import com.ad.cookgood.share_recipe.domain.usecase.GetSharedRecipeDetailUseCase
import com.ad.cookgood.shared.SnackBarUiState
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedRecipeViewModel @Inject constructor(
   private val getSharedRecipeDetailUseCase: GetSharedRecipeDetailUseCase,
   private val addFavoriteUseCase: AddFavoriteUseCase,
   private val removeFavoriteUseCase: RemoveFavoriteUseCase,
   private val getCurrentUserUseCase: GetCurrentUserUseCase,
   private val checkRecipeFavoriteUseCase: CheckRecipeFavoriteUseCase,
   stateHandle: SavedStateHandle
) : ViewModel() {

   private val sharedRecipeId: String =
      checkNotNull(stateHandle[SharedRecipeDetailScreen.sharedRecipeIdArg])
   private var currentUser: FirebaseUser? = null

   private val _sharedRecipeDetails = mutableStateOf<SharedRecipeDetails?>(null)
   val sharedRecipeDetails: State<SharedRecipeDetails?> = _sharedRecipeDetails

   private val _snackBarUiState = mutableStateOf(SnackBarUiState())
   val snackBarUiState: State<SnackBarUiState> = _snackBarUiState

   private val _isFavorite = mutableStateOf(false)
   val isFavorite: State<Boolean> = _isFavorite

   init {
      viewModelScope.launch {
         currentUser = getCurrentUserUseCase().first()
         _sharedRecipeDetails.value = getSharedRecipeDetail()
         checkRecipeFavoriteUseCase(sharedRecipeId, currentUser?.uid).collect {
            _isFavorite.value = it
         }
      }
   }

   suspend fun getSharedRecipeDetail() = getSharedRecipeDetailUseCase(sharedRecipeId)

   fun addFavorite() {
      viewModelScope.launch {
         addFavoriteUseCase(
            FavoriteRecipe(
               recipeId = sharedRecipeId,
               userId = currentUser?.uid ?: ""
            )
         )
         _snackBarUiState.value = _snackBarUiState.value.copy(
            message = "Đã thêm vào danh sách yêu thích",
            showSnackBar = true
         )
      }
   }

   fun removeFavorite() {
      viewModelScope.launch {
         removeFavoriteUseCase(
            FavoriteRecipe(
               recipeId = sharedRecipeId,
               userId = currentUser?.uid ?: ""
            )
         )
         _snackBarUiState.value = _snackBarUiState.value.copy(
            message = "Đã xóa khỏi danh sách yêu thích",
            showSnackBar = true
         )
      }
   }

   fun hideSnackBar() {
      _snackBarUiState.value = _snackBarUiState.value.copy(
         showSnackBar = false
      )
   }

   private companion object {
      const val TAG = "SharedRecipeViewModel"
   }

}