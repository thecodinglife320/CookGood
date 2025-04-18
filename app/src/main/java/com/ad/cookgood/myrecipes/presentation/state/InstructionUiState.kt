package com.ad.cookgood.myrecipes.presentation.state

data class InstructionUiState(
   override val id: Int = 0,
   override val name: String = "",
   override var stepNumber: Int = 0,
) : CommonUiState
