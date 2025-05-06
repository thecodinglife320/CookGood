package com.ad.cookgood.mycookbook.domain.usecase

import com.ad.cookgood.mycookbook.domain.MyRecipeRepository
import javax.inject.Inject

class GetMyRecipesUseCase @Inject constructor(
   private val repository: MyRecipeRepository
) {
   operator fun invoke() = repository.getMyRecipes()
}