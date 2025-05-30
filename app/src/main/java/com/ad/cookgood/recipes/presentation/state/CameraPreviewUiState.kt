package com.ad.cookgood.recipes.presentation.state

data class CameraPreviewUiState(
   val showCameraPreview: Boolean = false,
   val isCaptureForRecipe: Boolean = false
)