package com.ad.cookgood.recipes.presentation.state

data class SnackBarUiState(
   val message: String = "",
   val showSnackBar: Boolean = false,
   val isError: Boolean = false
)