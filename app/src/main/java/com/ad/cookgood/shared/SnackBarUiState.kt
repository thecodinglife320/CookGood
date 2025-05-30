package com.ad.cookgood.shared

data class SnackBarUiState(
   val message: String = "",
   val showSnackBar: Boolean = false,
   val isError: Boolean = false,
   val actionLabel: String? = null
)