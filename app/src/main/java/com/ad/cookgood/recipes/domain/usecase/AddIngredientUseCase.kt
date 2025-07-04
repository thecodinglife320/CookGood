package com.ad.cookgood.recipes.domain.usecase

import com.ad.cookgood.recipes.domain.RecipeRepository
import com.ad.cookgood.recipes.domain.model.Ingredient
import javax.inject.Inject

class AddIngredientUseCase @Inject constructor(
   private val recipeRepository: RecipeRepository,
) {
   suspend operator fun invoke(ingredient: Ingredient, recipeId: Long) =
      recipeRepository.insertIngredient(ingredient, recipeId)
}