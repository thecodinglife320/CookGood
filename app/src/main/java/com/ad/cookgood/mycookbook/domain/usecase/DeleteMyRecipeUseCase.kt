package com.ad.cookgood.mycookbook.domain.usecase

import com.ad.cookgood.mycookbook.domain.MyRecipeRepository
import com.ad.cookgood.mycookbook.domain.model.MyRecipe
import javax.inject.Inject

class DeleteMyRecipeUseCase @Inject constructor(
   private val repository: MyRecipeRepository
) {
   suspend operator fun invoke(myRecipe: MyRecipe) = repository.deleteMyRecipe(myRecipe)
}