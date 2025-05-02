package com.ad.cookgood.mycookbook.domain.usecase

import com.ad.cookgood.mycookbook.domain.MyRecipeRepository
import javax.inject.Inject

class GetMyRecipeUseCase @Inject constructor(
   private val repository: MyRecipeRepository
) {
   suspend operator fun invoke(recipeId: Long) = repository.getMyRecipeById(recipeId)
}