package com.ad.cookgood.like_recipe.domain.usecase

import com.ad.cookgood.like_recipe.domain.LikeRecipeRepository
import javax.inject.Inject

//GetFavoritesUseCase
class GetFavoritesUseCase @Inject constructor(
   private val repository: LikeRecipeRepository
) {
   operator fun invoke(userId: String) = repository.getFavorites(userId)
}