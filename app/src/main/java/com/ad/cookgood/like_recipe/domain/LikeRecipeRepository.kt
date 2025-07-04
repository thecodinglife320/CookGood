package com.ad.cookgood.like_recipe.domain

import com.ad.cookgood.like_recipe.domain.model.FavoriteRecipe
import com.ad.cookgood.share_recipe.domain.model.SharedRecipe
import kotlinx.coroutines.flow.Flow

interface LikeRecipeRepository {
   suspend fun addFavorite(favoriteRecipe: FavoriteRecipe)

   suspend fun removeFavorite(favoriteRecipe: FavoriteRecipe)

   fun getFavorites(userId: String): Flow<List<SharedRecipe>>

   fun isRecipeFavorite(recipeId: String, currentUserId: String?): Flow<Boolean>
}

