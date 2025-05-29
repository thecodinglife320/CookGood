package com.ad.cookgood.recipes.presentation.state

data class DialogUiState(
   val showDialog: Boolean = false,
   val message: String = "",
   val shouldShowRationale: Boolean = false,
)
