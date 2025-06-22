package com.ad.cookgood.mycookbook.presentaion.myrecipedetail

import android.app.Application
import android.net.Uri
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
import com.ad.cookgood.mycookbook.presentaion.state.toDomain
import com.ad.cookgood.navigation.data.MyRecipeDetailScreen
import com.ad.cookgood.profile.domain.GetCurrentUserUseCase
import com.ad.cookgood.recipes.domain.model.toRecipeUiState
import com.ad.cookgood.recipes.domain.model.toUiState
import com.ad.cookgood.recipes.presentation.state.toDomain
import com.ad.cookgood.share_recipe.data.FirebaseIngredient
import com.ad.cookgood.share_recipe.data.FirebaseInstruction
import com.ad.cookgood.share_recipe.data.FirebaseRecipe
import com.ad.cookgood.share_recipe.domain.ShareRecipeUseCase
import com.ad.cookgood.uploadimage.domain.UploadImageUseCase
import com.ad.cookgood.util.getAppWriteFileViewUri
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

   init {
      viewModelScope.launch {
         getCurrentUserUseCase().collect {
            _currentUser.value = it
         }
      }
   }

   //prepare state
   val myRecipeUiState =
      getMyRecipeUseCase(id)
         .map {
            it?.let {
               MyRecipeUiState(
                  id = it.id,
                  recipeUiState = it.recipe.toRecipeUiState().copy(
                     cookTimeMinutes = "${it.recipe.cookTime % 60} phút",
                     cookTimeHours = "${it.recipe.cookTime / 60} tiếng",
                     servings = "${it.recipe.serving} người"
                  )
               )
            }
         }
         .stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), MyRecipeUiState())

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

   fun shareRecipe() =
      viewModelScope.launch {
         var uploadedRecipeUri: Uri? = null
         myRecipeUiState.value!!.recipeUiState.uri?.let {
            val fileDetails = getFileDetailsFromUri(application, it)
            if (fileDetails != null) {
               val file = InputFile.fromBytes(
                  fileDetails.bytes,
                  fileDetails.filename!!,
                  fileDetails.mimeType ?: "application/octet-stream"
               )
               uploadImageUseCase(file, ID.unique())
                  .onSuccess {
                     uploadedRecipeUri = getAppWriteFileViewUri(
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

         val firebaseRecipe = FirebaseRecipe(
            recipe = myRecipeUiState.value!!.recipeUiState.toDomain().copy(
               uri = uploadedRecipeUri.toString()
            ),
            userId = currentUser.value?.uid
         )
         val firebaseInstructions = instructionUiStates.value.map {
            var uploadedInstructionUri: Uri? = null
            it.uri?.let {
               val fileDetails = getFileDetailsFromUri(application, it)
               if (fileDetails != null) {
                  val file = InputFile.fromBytes(
                     fileDetails.bytes,
                     fileDetails.filename!!,
                     fileDetails.mimeType ?: "application/octet-stream"
                  )
                  uploadImageUseCase(file, ID.unique())
                     .onSuccess {
                        uploadedInstructionUri = getAppWriteFileViewUri(
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
            FirebaseInstruction(
               instruction = it.toDomain().copy(
                  uri = uploadedInstructionUri
               )
            )
         }
         val firebaseIngredients = ingredientUiStates.value.map {
            FirebaseIngredient(
               ingredient = it.toDomain()
            )
         }
         shareRecipeUseCase(firebaseRecipe, firebaseInstructions, firebaseIngredients)
      }

   fun deleteMyRecipe() =
      viewModelScope.launch {
         deleteMyRecipeUseCase(myRecipeUiState.value!!.toDomain())
      }
}


