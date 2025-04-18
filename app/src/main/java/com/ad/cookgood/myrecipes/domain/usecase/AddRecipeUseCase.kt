package com.ad.cookgood.myrecipes.domain.usecase

import com.ad.cookgood.myrecipes.domain.RecipeRepository
import com.ad.cookgood.myrecipes.domain.model.Recipe
import javax.inject.Inject

class AddRecipeUseCase @Inject constructor(
   private val recipeRepository: RecipeRepository,
) {

   suspend operator fun invoke(recipe: Recipe) = recipeRepository.insertRecipe(recipe)
}

