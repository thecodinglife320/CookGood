package com.ad.cookgood.myrecipes.domain.usecase

import com.ad.cookgood.myrecipes.domain.model.Recipe

interface RecipeRepository {
   suspend fun insertRecipe(recipe: Recipe)
}