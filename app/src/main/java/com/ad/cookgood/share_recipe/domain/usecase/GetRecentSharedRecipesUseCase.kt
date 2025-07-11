package com.ad.cookgood.share_recipe.domain.usecase

import com.ad.cookgood.share_recipe.domain.ShareRecipeRepository
import com.ad.cookgood.share_recipe.domain.model.SharedRecipe
import javax.inject.Inject

class GetRecentSharedRecipesUseCase @Inject constructor(
   private val repository: ShareRecipeRepository
) {
   suspend operator fun invoke(limit: Int): List<SharedRecipe> {
      return repository.getRecentSharedRecipes(limit)
   }
}