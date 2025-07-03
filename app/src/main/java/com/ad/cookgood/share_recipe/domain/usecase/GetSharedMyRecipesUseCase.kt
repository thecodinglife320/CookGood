package com.ad.cookgood.share_recipe.domain.usecase

import com.ad.cookgood.share_recipe.domain.ShareRecipeRepository
import javax.inject.Inject

class GetSharedMyRecipesUseCase @Inject constructor(
   private val repository: ShareRecipeRepository
) {
   operator fun invoke(userId: String) = repository.getSharedMyRecipes(userId)
}