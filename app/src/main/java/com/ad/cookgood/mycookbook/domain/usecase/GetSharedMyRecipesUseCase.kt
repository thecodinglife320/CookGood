package com.ad.cookgood.mycookbook.domain.usecase

import com.ad.cookgood.mycookbook.domain.MyRecipeRepository
import javax.inject.Inject

class GetSharedMyRecipesUseCase @Inject constructor(
   private val repository: MyRecipeRepository
) {
   operator fun invoke(userId: String) = repository.getSharedMyRecipes(userId)
}