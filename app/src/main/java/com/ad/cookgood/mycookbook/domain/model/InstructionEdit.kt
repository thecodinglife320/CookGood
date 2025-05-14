package com.ad.cookgood.mycookbook.domain.model

import com.ad.cookgood.recipes.domain.model.Instruction

data class InstructionEdit(
   val id: Long,
   val instruction: Instruction,
)