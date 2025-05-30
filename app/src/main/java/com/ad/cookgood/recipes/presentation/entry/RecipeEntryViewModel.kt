package com.ad.cookgood.recipes.presentation.entry

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ad.cookgood.R
import com.ad.cookgood.captureimage.domain.GetCaptureUseCase
import com.ad.cookgood.captureimage.domain.GetPreviewUseCase
import com.ad.cookgood.captureimage.domain.TakePhotoUseCase
import com.ad.cookgood.recipes.domain.usecase.AddIngredientUseCase
import com.ad.cookgood.recipes.domain.usecase.AddInstructionUseCase
import com.ad.cookgood.recipes.domain.usecase.AddRecipeUseCase
import com.ad.cookgood.recipes.presentation.state.CameraPreviewUiState
import com.ad.cookgood.recipes.presentation.state.CommonUiState
import com.ad.cookgood.recipes.presentation.state.DialogUiState
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.InstructionUiState
import com.ad.cookgood.recipes.presentation.state.RecipeUiState
import com.ad.cookgood.recipes.presentation.state.toDomain
import com.ad.cookgood.shared.SnackBarUiState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus.Denied
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class)
@Suppress("PropertyName")
@HiltViewModel
open class RecipeEntryViewModel @Inject constructor(
   private val addRecipeUseCase: AddRecipeUseCase,
   protected val addIngredientUseCase: AddIngredientUseCase,
   protected val addInstructionUseCase: AddInstructionUseCase,
   getPreviewUseCase: GetPreviewUseCase,
   getCaptureUseCase: GetCaptureUseCase,
   private val takePhotoUseCase: TakePhotoUseCase,
   internal val application: Application
) : ViewModel() {

   //prepare state
   protected val _recipeUiState = mutableStateOf(
      RecipeUiState()
   )

   protected val _ingredientUiStates = mutableStateOf(
      listOf<IngredientUiState>()
   )

   protected val _instructionUiStates = mutableStateOf(
      listOf<InstructionUiState>()
   )

   protected val _snackBarUiState = MutableStateFlow(SnackBarUiState())
   val snackBarUiState: StateFlow<SnackBarUiState> = _snackBarUiState

   private val _cameraPreviewUiState = MutableStateFlow(CameraPreviewUiState())
   val cameraPreviewUiState: StateFlow<CameraPreviewUiState> = _cameraPreviewUiState

   private var instructionNeedTakePhoto: Int = 0

   private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
   val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest

   private val _dialogUiState = MutableStateFlow<DialogUiState>(DialogUiState())
   val dialogUiState: StateFlow<DialogUiState> = _dialogUiState

   private val cameraPreviewUseCase = getPreviewUseCase()

   private val cameraCaptureUseCase = getCaptureUseCase()

   //expose state
   val ingredientUiStates: State<List<IngredientUiState>> get() = _ingredientUiStates

   val instructionUiStates: State<List<InstructionUiState>> get() = _instructionUiStates

   val recipeUiState: State<RecipeUiState> = _recipeUiState

   //coroutine exception handle
   internal val coroutineExceptionHandler =
      CoroutineExceptionHandler { _, ex ->
         _snackBarUiState.value = _snackBarUiState.value.copy(
            showSnackBar = true,
            isError = true,
            message = application.getString(R.string.recipe_entry_error)
         )
      }

   fun addCommonUiState(uiState: CommonUiState) {
      when (uiState) {
         is IngredientUiState -> _ingredientUiStates.value = _ingredientUiStates.value + uiState

         is InstructionUiState -> {
            var stepNumber = (_instructionUiStates.value.maxOfOrNull { it.stepNumber } ?: 0) + 1
            _instructionUiStates.value = _instructionUiStates.value + uiState.apply {
               this.stepNumber = stepNumber
            }
         }
      }
   }

   fun updateCommonUiState(uiState: CommonUiState, newName: String) {
      when (uiState) {
         is IngredientUiState -> _ingredientUiStates.value = _ingredientUiStates.value.map {
            if (it.id == uiState.id) it.copy(name = newName) else it
         }

         is InstructionUiState -> _instructionUiStates.value = _instructionUiStates.value.map {
            if (it.id == uiState.id) it.copy(name = newName) else it
         }
      }
   }

   fun removeCommonUiState(uiState: CommonUiState) {
      when (uiState) {
         is InstructionUiState -> {
            _instructionUiStates.value =
               (_instructionUiStates.value - uiState).mapIndexed { index, uiState ->
                  uiState.apply { stepNumber = index + 1 }
               }
         }

         is IngredientUiState -> _ingredientUiStates.value = _ingredientUiStates.value - uiState
      }
   }

   open fun saveRecipe() {
      viewModelScope.launch(coroutineExceptionHandler) {
         val recipeId = addRecipeUseCase(_recipeUiState.value.toDomain())

         _ingredientUiStates.value.forEach {
            addIngredientUseCase(it.toDomain(), recipeId)
         }

         _instructionUiStates.value.forEach {
            addInstructionUseCase(it.toDomain(), recipeId = recipeId)
         }

         _snackBarUiState.value = _snackBarUiState.value.copy(
            showSnackBar = true,
            isError = false,
            message = application.getString(R.string.recipe_entry_success)
         )
      }
   }

   fun onTitleChange(title: String) {
      _recipeUiState.value = _recipeUiState.value.copy(
         title = title
      )
   }

   fun onBriefChange(brief: String) {
      _recipeUiState.value = _recipeUiState.value.copy(
         brief = brief
      )
   }

   fun onServingChange(serving: String) {
      _recipeUiState.value = _recipeUiState.value.copy(
         servings = serving
      )
   }

   fun onHourChange(hour: String) {
      _recipeUiState.value = _recipeUiState.value.copy(
         cookTimeHours = hour
      )
   }

   fun onMinuteChange(minute: String) {
      _recipeUiState.value = _recipeUiState.value.copy(
         cookTimeMinutes = minute
      )
   }

   fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
      viewModelScope.launch {
         cameraPreviewUseCase.setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.update { newSurfaceRequest }
         }

         val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)

         processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            DEFAULT_BACK_CAMERA,
            cameraPreviewUseCase,
            cameraCaptureUseCase
         )

         // Keep bound until cancellation
         try {
            awaitCancellation()
         } finally {
            processCameraProvider.unbindAll()
         }
      }
   }

   fun onTakePhotoInstruction() {
      viewModelScope.launch {
         _instructionUiStates.value = _instructionUiStates.value.map {
            if (it.id == instructionNeedTakePhoto) it.copy(
               uri = takePhotoUseCase()
            ) else it
         }
      }
   }

   fun onPrepareTakePhotoRecipe(permissionState: PermissionState) {
      _cameraPreviewUiState.value = _cameraPreviewUiState.value.copy(
         showCameraPreview = handelPermission(permissionState),
         isCaptureForRecipe = true
      )
   }

   private fun handelPermission(permissionState: PermissionState): Boolean {
      if (permissionState.status is Denied) {
         _dialogUiState.value = _dialogUiState.value.copy(
            showDialog = true
         )
         if ((permissionState.status as Denied).shouldShowRationale) {
            _dialogUiState.value = _dialogUiState.value.copy(
               message = application.getString(R.string.camera_permisson_after_denied),
               shouldShowRationale = true
            )
         } //di den cai dat
         else {
            _dialogUiState.value = _dialogUiState.value.copy(
               message = application.getString(R.string.camera_permission),
               shouldShowRationale = false
            )
         }//hoi cap quyen
         return false
      } else {
         return true
      }
   }

   fun onPrepareTakePhotoInstruction(id: Int, permissionState: PermissionState) {
      _cameraPreviewUiState.value = _cameraPreviewUiState.value.copy(
         showCameraPreview = handelPermission(permissionState),
         isCaptureForRecipe = false
      )
      instructionNeedTakePhoto = id
   }

   @SuppressLint("RestrictedApi")
   fun onTakePhotoRecipe() {
      cameraCaptureUseCase
      viewModelScope.launch {
         _recipeUiState.value = _recipeUiState.value.copy(
            uri = takePhotoUseCase()
         )
      }
   }

   fun onDismissCameraPreview() {
      _cameraPreviewUiState.value = _cameraPreviewUiState.value.copy(
         showCameraPreview = false
      )
   }

   fun onDismissDialog() {
      _dialogUiState.value = _dialogUiState.value.copy(
         showDialog = false
      )
   }

   fun onDismissSnackBar() {
      _snackBarUiState.value = _snackBarUiState.value.copy(
         showSnackBar = false
      )
   }

   private companion object {
      //const val TAG = "RecipeEntryViewModel"
   }
}