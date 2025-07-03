package com.ad.cookgood.share_recipe.domain.usecase

import com.ad.cookgood.share_recipe.domain.ShareRecipeRepository
import com.ad.cookgood.share_recipe.domain.model.SharedIngredient
import com.ad.cookgood.share_recipe.domain.model.SharedInstruction
import com.ad.cookgood.share_recipe.domain.model.SharedRecipe
import javax.inject.Inject

class ShareRecipeUseCase @Inject constructor(
   private val shareRecipeRepository: ShareRecipeRepository
) {
   operator fun invoke(
      sharedRecipe: SharedRecipe,
      sharedInstructions: List<SharedInstruction>,
      sharedIngredients: List<SharedIngredient>
   ) {
      shareRecipeRepository.shareRecipe(sharedRecipe, sharedInstructions, sharedIngredients)
   }
}