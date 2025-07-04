package com.ad.cookgood.like_recipe.domain.usecase

import com.ad.cookgood.like_recipe.domain.LikeRecipeRepository
import com.ad.cookgood.like_recipe.domain.model.FavoriteRecipe
import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(
   private val likeRecipeRepository: LikeRecipeRepository
) {
   suspend operator fun invoke(favoriteRecipe: FavoriteRecipe) {
      likeRecipeRepository.removeFavorite(favoriteRecipe)
   }
}