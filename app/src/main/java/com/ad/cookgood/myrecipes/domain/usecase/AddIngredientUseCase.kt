package com.ad.cookgood.myrecipes.domain.usecase

import com.ad.cookgood.myrecipes.domain.model.Ingredient
import javax.inject.Inject

class AddIngredientUseCase @Inject constructor(
   private val recipeRepositoryImpl: RecipeRepository,
) {

   suspend operator fun invoke(ingredient: Ingredient, recipeId: Long) =
      recipeRepositoryImpl.insertIngredient(ingredient, recipeId)
}