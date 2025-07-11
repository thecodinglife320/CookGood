package com.ad.cookgood.mycookbook.domain.usecase

import com.ad.cookgood.mycookbook.domain.MyRecipeRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetInstructionsOfMyRecipeUseCase @Inject constructor(
   private val repository: MyRecipeRepository
) {
   operator fun invoke(recipeId: Long) = repository.getInstructionsByRecipeId(recipeId).map {
      it.sortedBy { it.stepNumber }
   }
}