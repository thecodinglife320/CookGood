package com.ad.cookgood.mycookbook.domain.usecase

import com.ad.cookgood.mycookbook.domain.MyRecipeRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetInstructionEditsUseCase @Inject constructor(
   private val repository: MyRecipeRepository
) {
   suspend operator fun invoke(recipeId: Long) = repository.getInstructionEdits(recipeId).map {
      it.sortedBy { it.instruction.stepNumber }
   }
}