package com.ad.cookgood.recipes.domain.usecase

import com.ad.cookgood.recipes.domain.RecipeRepository
import com.ad.cookgood.recipes.domain.model.Instruction
import javax.inject.Inject

class AddInstructionUseCase @Inject constructor(
   private val recipeRepository: RecipeRepository,
) {
   suspend operator fun invoke(instruction: Instruction, recipeId: Long) =
      recipeRepository.insertInstruction(instruction = instruction, recipeId)
}