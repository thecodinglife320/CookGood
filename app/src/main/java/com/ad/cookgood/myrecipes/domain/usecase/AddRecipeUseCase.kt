package com.ad.cookgood.myrecipes.domain.usecase

import com.ad.cookgood.myrecipes.data.local.RecipeRepositoryImpl
import com.ad.cookgood.myrecipes.domain.model.Ingredient
import com.ad.cookgood.myrecipes.domain.model.Recipe
import javax.inject.Inject

class AddRecipeUseCase @Inject constructor(
   private val recipeRepositoryImpl: RecipeRepository,
) {

   suspend operator fun invoke(recipe: Recipe) = recipeRepositoryImpl.insertRecipe(recipe)
}

class AddIngredientUseCase @Inject constructor(
   private val recipeRepositoryImpl: RecipeRepositoryImpl,
) {

   suspend operator fun invoke(ingredient: Ingredient, recipeId: Long) =
      let {
         recipeRepositoryImpl.insertIngredient(ingredient, recipeId)
      }
}