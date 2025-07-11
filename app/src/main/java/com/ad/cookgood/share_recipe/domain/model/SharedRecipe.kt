package com.ad.cookgood.share_recipe.domain.model

import com.ad.cookgood.recipes.domain.model.Recipe
import com.ad.cookgood.share_recipe.data.FirebaseRecipe
import com.ad.cookgood.util.normalizeStringForSearch

data class SharedRecipe(
   val id: String,
   val recipe: Recipe,
   val userId: String,
   val uploadAt: Long
)

fun SharedRecipe.toRemote() =
   FirebaseRecipe(
      recipe = recipe,
      userId = userId,
      normalizedTitle = normalizeStringForSearch(recipe.title),
   )