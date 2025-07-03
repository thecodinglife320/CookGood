package com.ad.cookgood.share_recipe.domain.model

import com.ad.cookgood.recipes.domain.model.Instruction
import com.ad.cookgood.share_recipe.data.FirebaseInstruction

data class SharedInstruction(
   val id: String,
   val instruction: Instruction,
)

fun SharedInstruction.toRemote() =
   FirebaseInstruction(
      instruction = instruction
   )
