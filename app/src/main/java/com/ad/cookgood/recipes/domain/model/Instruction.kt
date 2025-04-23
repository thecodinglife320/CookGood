package com.ad.cookgood.recipes.domain.model

import com.ad.cookgood.recipes.data.local.instruction.LocalInstruction

data class Instruction(
   val stepNumber: Int = 0,
   val name: String = "",
)

fun Instruction.toLocal(recipeId: Long) =
   LocalInstruction(
      name = name,
      strepNumber = stepNumber,
      recipeId = recipeId
   )
