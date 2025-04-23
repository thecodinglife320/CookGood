package com.ad.cookgood.recipes.presentation.state

import com.ad.cookgood.recipes.domain.model.Instruction

data class InstructionUiState(
   override val id: Int = 0,
   override val name: String = "",
   var stepNumber: Int = 0,
) : CommonUiState

fun InstructionUiState.toDomain() =
   Instruction(
      stepNumber = stepNumber,
      name = name
   )
