package com.ad.cookgood.recipes.domain.model

import androidx.core.net.toUri
import com.ad.cookgood.recipes.data.local.instruction.LocalInstruction
import com.ad.cookgood.recipes.presentation.state.InstructionUiState

data class Instruction(
   val stepNumber: Int = 0,
   val name: String = "",
   val photo: String?
)

fun Instruction.toLocal(recipeId: Long) =
   LocalInstruction(
      name = name,
      strepNumber = stepNumber,
      recipeId = recipeId,
      photo = photo,
   )

fun Instruction.toUiState() =
   InstructionUiState(
      name = name,
      stepNumber = stepNumber,
      uri = photo?.toUri(),
   )
