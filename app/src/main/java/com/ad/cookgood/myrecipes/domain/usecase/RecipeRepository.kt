package com.ad.cookgood.myrecipes.domain.usecase

import com.ad.cookgood.myrecipes.domain.model.Ingredient
import com.ad.cookgood.myrecipes.domain.model.Recipe

interface RecipeRepository {
   suspend fun insertRecipe(recipe: Recipe): Long
   suspend fun insertIngredient(ingredient: Ingredient, recipeId: Long)
}