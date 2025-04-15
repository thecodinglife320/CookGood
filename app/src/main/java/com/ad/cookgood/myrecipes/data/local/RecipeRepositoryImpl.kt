package com.ad.cookgood.myrecipes.data.local

import com.ad.cookgood.myrecipes.domain.model.Ingredient
import com.ad.cookgood.myrecipes.domain.model.Recipe
import com.ad.cookgood.myrecipes.domain.usecase.RecipeRepository
import com.ad.cookgood.myrecipes.toLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
   private val recipeDao: RecipeDao,
   private val ingredientDao: IngredientDao,
) : RecipeRepository {

   override suspend fun insertRecipe(recipe: Recipe) =
      withContext(Dispatchers.IO) {
         recipeDao.insertRecipe(recipe.toLocal())
      }

   override suspend fun insertIngredient(ingredient: Ingredient, recipeId: Long) {
      withContext(Dispatchers.IO) {
         ingredientDao.insertIngredient(ingredient.toLocal(recipeId))
      }
   }
}