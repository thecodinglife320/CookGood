package com.ad.cookgood.like_recipe.domain.usecase

import com.ad.cookgood.like_recipe.domain.LikeRecipeRepository
import com.ad.cookgood.like_recipe.domain.model.FavoriteRecipe
import javax.inject.Inject

/**
 * Use case for adding a recipe to the user's favorites.
 *
 * This class encapsulates the business logic for marking a recipe as a favorite.
 * It interacts with the [com.ad.cookgood.like_recipe.domain.LikeRecipeRepository] to persist the favorite recipe.
 *
 * @param likeRecipeRepository The repository responsible for managing favorite recipes.
 *///usecase
class AddFavoriteUseCase @Inject constructor(
   private val likeRecipeRepository: LikeRecipeRepository
) {

   suspend operator fun invoke(favoriteRecipe: FavoriteRecipe) =
      likeRecipeRepository.addFavorite(favoriteRecipe)

}