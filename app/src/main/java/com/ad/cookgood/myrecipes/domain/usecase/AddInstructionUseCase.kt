package com.ad.cookgood.myrecipes.domain.usecase

import com.ad.cookgood.myrecipes.domain.model.Instruction
import javax.inject.Inject

class AddInstructionUseCase @Inject constructor(
   private val recipeRepositoryImpl: RecipeRepository,
) {
   suspend operator fun invoke(instruction: Instruction, recipeId: Long) =
      recipeRepositoryImpl.insertInstruction(instruction = instruction, recipeId)
}