package com.ad.cookgood.like_recipe.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckRecipeFavoriteUseCase @Inject constructor(
   private val likeRecipeRepository: LikeRecipeRepository
) {
   operator fun invoke(recipeId: String, currentUserId: String?): Flow<Boolean> {
      return likeRecipeRepository.isRecipeFavorite(recipeId, currentUserId)
   }
}