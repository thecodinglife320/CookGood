package com.ad.cookgood.myrecipes.presentation.state

interface CommonUiState {
   val id: Int
   val name: String
   val stepNumber: Int
      get() = 0
}