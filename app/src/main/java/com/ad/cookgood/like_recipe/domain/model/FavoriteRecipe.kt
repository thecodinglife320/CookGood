package com.ad.cookgood.like_recipe.domain.model

import com.ad.cookgood.like_recipe.data.FirebaseFavoriteRecipe

data class FavoriteRecipe(
   val recipeId: String = "",
   val favoriteAt: Long = System.currentTimeMillis(),
   val userId: String = ""
)

fun FavoriteRecipe.toData() =
   FirebaseFavoriteRecipe(
      recipeId = recipeId,
      favoriteAt = favoriteAt
   )