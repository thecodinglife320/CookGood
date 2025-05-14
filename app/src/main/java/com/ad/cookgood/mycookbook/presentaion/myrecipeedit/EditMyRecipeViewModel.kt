package com.ad.cookgood.mycookbook.presentaion.myrecipeedit

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.captureimage.domain.StartCameraUseCase
import com.ad.cookgood.captureimage.domain.StopCameraUseCase
import com.ad.cookgood.captureimage.domain.TakePhotoUseCase
import com.ad.cookgood.mycookbook.domain.model.IngredientEdit
import com.ad.cookgood.mycookbook.domain.model.InstructionEdit
import com.ad.cookgood.mycookbook.domain.model.MyRecipe
import com.ad.cookgood.mycookbook.domain.usecase.DeleteIngredientUseCase
import com.ad.cookgood.mycookbook.domain.usecase.DeleteInstructionUseCase
import com.ad.cookgood.mycookbook.domain.usecase.GetIngredientEditsUseCase
import com.ad.cookgood.mycookbook.domain.usecase.GetInstructionEditsUseCase
import com.ad.cookgood.mycookbook.domain.usecase.GetMyRecipeUseCase
import com.ad.cookgood.mycookbook.domain.usecase.UpdateMyRecipeUseCase
import com.ad.cookgood.navigation.data.MyRecipeDetailScreen
import com.ad.cookgood.recipes.domain.model.toRecipeUiState
import com.ad.cookgood.recipes.domain.usecase.AddIngredientUseCase
import com.ad.cookgood.recipes.domain.usecase.AddInstructionUseCase
import com.ad.cookgood.recipes.domain.usecase.AddRecipeUseCase
import com.ad.cookgood.recipes.presentation.entry.RecipeEntryViewModel
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.InstructionUiState
import com.ad.cookgood.recipes.presentation.state.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
   private val updateMyRecipeUseCase: UpdateMyRecipeUseCase,
   private val getIngredientEditsUseCase: GetIngredientEditsUseCase,
   private val getInstructionEditsUseCase: GetInstructionEditsUseCase,
   private val deleteIngredientUseCase: DeleteIngredientUseCase,
   private val deleteInstructionUseCase: DeleteInstructionUseCase
) : RecipeEntryViewModel(
   addRecipeUseCase,
   addIngredientUseCase,
   addInstructionUseCase,
   startCameraUseCase,
   stopCameraUseCase,
   takePhotoUseCase
) {

   private val id = stateHandle.get<Long>(MyRecipeDetailScreen.recipeIdArg) ?: 0

   private var ingredientsOld = listOf<IngredientUiState>()

   private var instructionsOld = listOf<InstructionUiState>()

   init {
      viewModelScope.launch {
         _recipeUiState.value = getMyRecipeUseCase(id)
            .filterNotNull()
            .first()
            .recipe.toRecipeUiState()

         _ingredientUiStates.value =
            getIngredientEditsUseCase(id).map {
               it.map {
                  IngredientUiState(it.id.toInt(), it.ingredient.name)
               }
            }.first().also {
               ingredientsOld = it
            }

         _instructionUiStates.value =
            getInstructionEditsUseCase(id).map {
               it.map {
                  InstructionUiState(
                     it.id.toInt(),
                     it.instruction.name,
                     stepNumber = it.instruction.stepNumber,
                     uri = it.instruction.uri
                  )
               }
            }.first().also {
               instructionsOld = it
            }
      }
   }

   companion object {
      const val TAG = "EditMyRecipeViewModel"
   }

   override fun saveRecipe() {
      viewModelScope.launch(coroutineExceptionHandler) {

         updateMyRecipeUseCase(
            MyRecipe(
            id = id,
            recipe = _recipeUiState.value.toDomain()
            )
         )

         val ingredientsNew = _ingredientUiStates.value.subtract(ingredientsOld)
         val ingredientsDelete = ingredientsOld.subtract(_ingredientUiStates.value)
         val instructionsNew = _instructionUiStates.value.subtract(instructionsOld)
         val instructionsDelete = instructionsOld.subtract(_instructionUiStates.value)

         ingredientsDelete.forEach {
            deleteIngredientUseCase(
               IngredientEdit(
                  it.id.toLong(),
                  it.toDomain()
               ), id
            )
         }

         ingredientsNew.forEach {
            addIngredientUseCase(it.toDomain(), id)
         }

         instructionsDelete.forEach {
            deleteInstructionUseCase(
               InstructionEdit(
                  it.id.toLong(),
                  it.toDomain()
               ), id
            )
         }

         instructionsNew.forEach {
            addInstructionUseCase(it.toDomain(), id)
         }

         _successMessage.value = "da cap nhat"

         Log.d(TAG, "delete: $ingredientsDelete")
         Log.d(TAG, "new: $ingredientsNew")
      }
   }
}