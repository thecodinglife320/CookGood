package com.ad.cookgood.myrecipes.domain.usecase

import com.ad.cookgood.myrecipes.data.local.LocalRecipeRepository
import com.ad.cookgood.myrecipes.domain.model.Recipe
import javax.inject.Inject

class AddRecipeUseCase @Inject constructor(
   private val recipeRepository: LocalRecipeRepository,
) {

   suspend operator fun invoke(recipe: Recipe) = recipeRepository.insertRecipe(recipe)
}