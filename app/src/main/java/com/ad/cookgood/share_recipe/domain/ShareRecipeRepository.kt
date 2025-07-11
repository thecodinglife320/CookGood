package com.ad.cookgood.share_recipe.domain

import com.ad.cookgood.share_recipe.domain.model.SharedIngredient
import com.ad.cookgood.share_recipe.domain.model.SharedInstruction
import com.ad.cookgood.share_recipe.domain.model.SharedRecipe
import com.ad.cookgood.share_recipe.domain.model.SharedRecipeDetails
import kotlinx.coroutines.flow.Flow

interface ShareRecipeRepository {
   fun shareRecipe(
      sharedRecipe: SharedRecipe,
      sharedInstructions: List<SharedInstruction>,
      sharedIngredients: List<SharedIngredient>
   )
   suspend fun getSharedRecipe(sharedRecipeId: String): SharedRecipeDetails?
   fun getSharedMyRecipes(userId: String): Flow<List<SharedRecipe>>
   suspend fun searchRecipesByName(queryText: String): List<SharedRecipe>
   suspend fun getRecentSharedRecipes(limit: Int): List<SharedRecipe>
}

