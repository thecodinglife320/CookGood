package com.ad.cookgood.recipes.domain.model

import android.net.Uri
import com.ad.cookgood.recipes.data.local.instruction.LocalInstruction
import com.ad.cookgood.recipes.presentation.state.InstructionUiState

data class Instruction(
   val stepNumber: Int = 0,
   val name: String = "",
   val uri: Uri?
)

fun Instruction.toLocal(recipeId: Long) =
   LocalInstruction(
      name = name,
      strepNumber = stepNumber,
      recipeId = recipeId,
      uri = uri,
   )

fun Instruction.toUiState() =
   InstructionUiState(
      name = name,
      stepNumber = stepNumber,
      uri = uri
   )
