package com.ad.cookgood.mycookbook.presentaion.myrecipedetail

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.BuildConfig
import com.ad.cookgood.mycookbook.domain.usecase.DeleteMyRecipeUseCase
import com.ad.cookgood.mycookbook.domain.usecase.GetIngredientsOfMyRecipeUseCase
import com.ad.cookgood.mycookbook.domain.usecase.GetInstructionsOfMyRecipeUseCase
import com.ad.cookgood.mycookbook.domain.usecase.GetMyRecipeUseCase
import com.ad.cookgood.mycookbook.presentaion.state.MyRecipeUiState
import com.ad.cookgood.mycookbook.presentaion.state.ShareConfirmDialogUiState
import com.ad.cookgood.mycookbook.presentaion.state.toDomain
import com.ad.cookgood.navigation.data.MyRecipeDetailScreen
import com.ad.cookgood.profile.domain.GetCurrentUserUseCase
import com.ad.cookgood.recipes.domain.model.toRecipeUiState
import com.ad.cookgood.recipes.domain.model.toUiState
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.InstructionUiState
import com.ad.cookgood.recipes.presentation.state.toDomain
import com.ad.cookgood.share_recipe.domain.model.SharedIngredient
import com.ad.cookgood.share_recipe.domain.model.SharedInstruction
import com.ad.cookgood.share_recipe.domain.model.SharedRecipe
import com.ad.cookgood.share_recipe.domain.usecase.ShareRecipeUseCase
import com.ad.cookgood.uploadimage.domain.UploadImageUseCase
import com.ad.cookgood.util.getAppWriteFileViewUrl
import com.ad.cookgood.util.getFileDetailsFromUri
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appwrite.ID
import io.appwrite.models.InputFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecipeViewModel @Inject constructor(
   getMyRecipeUseCase: GetMyRecipeUseCase,
   getInstructionsOfMyRecipeUseCase: GetInstructionsOfMyRecipeUseCase,
   getIngredientsOfMyRecipeUseCase: GetIngredientsOfMyRecipeUseCase,
   private val deleteMyRecipeUseCase: DeleteMyRecipeUseCase,
   private val shareRecipeUseCase: ShareRecipeUseCase,
   private val uploadImageUseCase: UploadImageUseCase,
   private val application: Application,
   private val getCurrentUserUseCase: GetCurrentUserUseCase,
   stateHandle: SavedStateHandle,
) : ViewModel() {

   private val id = stateHandle.get<Long>(MyRecipeDetailScreen.recipeIdArg) ?: 0
   private val _currentUser: MutableStateFlow<FirebaseUser?> = MutableStateFlow(null)
   val currentUser: StateFlow<FirebaseUser?> = _currentUser

   private val _shareConfirmDialogUiState = MutableStateFlow(ShareConfirmDialogUiState())
   val shareConfirmDialogUiState: StateFlow<ShareConfirmDialogUiState> = _shareConfirmDialogUiState

   init {
      viewModelScope.launch {
         getCurrentUserUseCase().collect {
            _currentUser.value = it
         }
      }
   }

   //prepare state
   val myRecipeUiState = getMyRecipeUseCase(id)
      .map {
         it?.let {
            MyRecipeUiState(
               id = it.id,
               recipeUiState = it.recipe.toRecipeUiState().copy(
                  cookTimeMinutes = "${it.recipe.cookTime % 60}",
                  cookTimeHours = "${it.recipe.cookTime / 60}",
                  servings = "${it.recipe.serving}"
               )
            )
         }
      }.stateIn(
         viewModelScope,
         SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
         null
      )

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
      const val TAG = "MyRecipeViewModel"
   }

   fun onShareConfirmDialogDismiss() {
      _shareConfirmDialogUiState.update {
         it.copy(
            isVisible = false
         )
      }
   }

   fun onLaunchShareConfirmDialog() {
      _shareConfirmDialogUiState.update {
         it.copy(
            isVisible = true
         )
      }
   }

   fun onShareConfirmDialogConfirm() {
      viewModelScope.launch {
         _shareConfirmDialogUiState.update {
            it.copy(
               isSharing = true
            )
         }
         shareRecipe()
         _shareConfirmDialogUiState.update {
            it.copy(
               isSharing = false
            )
         }
      }
   }

   private suspend fun shareRecipe() {
      var uploadedRecipePhoto: String? = null
      myRecipeUiState.value!!.recipeUiState.uri?.let {
         val fileDetails = getFileDetailsFromUri(application, it)
         if (fileDetails != null) {
            val file = InputFile.fromBytes(
               fileDetails.bytes,
               fileDetails.filename!!,
               fileDetails.mimeType ?: "application/octet-stream"
            )
            uploadImageUseCase(file, ID.unique(), BuildConfig.APPWRITE_BUCKET_ID)
               .onSuccess {
                  uploadedRecipePhoto = getAppWriteFileViewUrl(
                     bucketId = BuildConfig.APPWRITE_BUCKET_ID,
                     fileId = it.id,
                     projectId = BuildConfig.APPWRITE_PROJECT_ID
                  )
                  Log.d(TAG, "uploadImage: $it")
               }
               .onFailure {
                  Log.e(TAG, it.message.toString())
               }
         }
      }

      val sharedInstructions = instructionUiStates.value.map {
         var uploadedInstructionPhoto: String? = null
         it.uri?.let {
            val fileDetails = getFileDetailsFromUri(application, it)
            if (fileDetails != null) {
               val file = InputFile.fromBytes(
                  fileDetails.bytes,
                  fileDetails.filename!!,
                  fileDetails.mimeType ?: "application/octet-stream"
               )
               uploadImageUseCase(file, ID.unique(), BuildConfig.APPWRITE_BUCKET_ID)
                  .onSuccess {
                     uploadedInstructionPhoto = getAppWriteFileViewUrl(
                        bucketId = BuildConfig.APPWRITE_BUCKET_ID,
                        fileId = it.id,
                        projectId = BuildConfig.APPWRITE_PROJECT_ID
                     )
                  }
                  .onFailure {
                     Log.e(TAG, it.message.toString())
                  }
            }
         }
         it.toSharedInstruction(
            uploadedInstructionPhoto
         )
      }

      val sharedIngredients = ingredientUiStates.value.map {
         it.toSharedIngredient()
      }

      shareRecipeUseCase(
         myRecipeUiState.value!!.toSharedRecipe(
            _currentUser.value!!.uid,
            uploadedRecipePhoto
         ),
         sharedInstructions,
         sharedIngredients
      )
   }

   fun deleteMyRecipe() =
      viewModelScope.launch {
         deleteMyRecipeUseCase(myRecipeUiState.value!!.toDomain())
      }
}

fun MyRecipeUiState.toSharedRecipe(userId: String, uploadedRecipePhoto: String? = null) =
   SharedRecipe(
      recipe = recipeUiState.toDomain().copy(photo = uploadedRecipePhoto),
      userId = userId,
      id = "",
      uploadAt = 0,
   )

fun IngredientUiState.toSharedIngredient() =
   SharedIngredient(
      ingredient = toDomain(),
      id = ""
   )

fun InstructionUiState.toSharedInstruction(uploadedInstructionPhoto: String?) =
   SharedInstruction(
      id = "",
      instruction = toDomain().copy(photo = uploadedInstructionPhoto)
   )


