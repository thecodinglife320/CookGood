package com.ad.cookgood.recipes.domain.usecase

import com.ad.cookgood.recipes.domain.RecipeRepository
import com.ad.cookgood.recipes.domain.model.Recipe
import javax.inject.Inject

class AddRecipeUseCase @Inject constructor(
   private val recipeRepository: RecipeRepository,
) {

   suspend operator fun invoke(recipe: Recipe) = recipeRepository.insertRecipe(recipe)
}

