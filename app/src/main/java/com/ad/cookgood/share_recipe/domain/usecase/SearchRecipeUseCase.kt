package com.ad.cookgood.share_recipe.domain.usecase

import com.ad.cookgood.share_recipe.domain.ShareRecipeRepository
import com.ad.cookgood.share_recipe.domain.model.SharedRecipe
import javax.inject.Inject

class SearchRecipeUseCase @Inject constructor(
   private val repository: ShareRecipeRepository
) {
   suspend operator fun invoke(queryText: String): List<SharedRecipe> {
      return repository.searchRecipesByName(queryText)
   }
}