package com.ad.cookgood.myrecipes.data.local

import com.ad.cookgood.myrecipes.domain.model.Recipe
import com.ad.cookgood.myrecipes.domain.usecase.RecipeRepository
import com.ad.cookgood.myrecipes.toLocalRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalRecipeRepository @Inject constructor(
   private val recipeDao: RecipeDao,
) : RecipeRepository {
   override suspend fun insertRecipe(recipe: Recipe) =
      withContext(Dispatchers.IO) {
         recipeDao.insertRecipe(recipe.toLocalRecipe())
      }
}