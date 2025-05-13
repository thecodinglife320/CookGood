package com.ad.cookgood.mycookbook.presentaion.myrecipeedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.captureimage.domain.StartCameraUseCase
import com.ad.cookgood.captureimage.domain.StopCameraUseCase
import com.ad.cookgood.captureimage.domain.TakePhotoUseCase
import com.ad.cookgood.mycookbook.domain.model.MyRecipe
import com.ad.cookgood.mycookbook.domain.usecase.GetMyRecipeUseCase
import com.ad.cookgood.mycookbook.domain.usecase.UpdateMyRecipeUseCase
import com.ad.cookgood.navigation.data.MyRecipeDetailScreen
import com.ad.cookgood.recipes.domain.model.toRecipeUiState
import com.ad.cookgood.recipes.domain.usecase.AddIngredientUseCase
import com.ad.cookgood.recipes.domain.usecase.AddInstructionUseCase
import com.ad.cookgood.recipes.domain.usecase.AddRecipeUseCase
import com.ad.cookgood.recipes.presentation.entry.RecipeEntryViewModel
import com.ad.cookgood.recipes.presentation.state.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMyRecipeViewModel @Inject constructor(
   addRecipeUseCase: AddRecipeUseCase,
   addIngredientUseCase: AddIngredientUseCase,
   addInstructionUseCase: AddInstructionUseCase,
   startCameraUseCase: StartCameraUseCase,
   stopCameraUseCase: StopCameraUseCase,
   takePhotoUseCase: TakePhotoUseCase,
   getMyRecipeUseCase: GetMyRecipeUseCase,
   stateHandle: SavedStateHandle,
   private val updateMyRecipeUseCase: UpdateMyRecipeUseCase
) : RecipeEntryViewModel(
   addRecipeUseCase,
   addIngredientUseCase,
   addInstructionUseCase,
   startCameraUseCase,
   stopCameraUseCase,
   takePhotoUseCase
) {

   private val id = stateHandle.get<Long>(MyRecipeDetailScreen.recipeIdArg) ?: 0

   init {
      viewModelScope.launch {
         _recipeUiState.value = getMyRecipeUseCase(id)
            .filterNotNull()
            .first()
            .recipe.toRecipeUiState()
      }
   }

   companion object {
      const val TAG = "EditMyRecipeViewModel"
   }

   override fun saveRecipe() {
      viewModelScope.launch(coroutineExceptionHandler) {
         val myRecipe = MyRecipe(
            id = id,
            recipe = _recipeUiState.value.toDomain()
         )
         updateMyRecipeUseCase(myRecipe)

         _successMessage.value = "da cap nhat"
      }
   }
}