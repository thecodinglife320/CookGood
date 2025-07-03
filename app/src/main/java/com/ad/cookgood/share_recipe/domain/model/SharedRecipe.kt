package com.ad.cookgood.share_recipe.domain.model

import com.ad.cookgood.recipes.domain.model.Recipe
import com.ad.cookgood.share_recipe.data.FirebaseRecipe

data class SharedRecipe(
   val id: String,
   val recipe: Recipe,
   val userId: String
)

fun SharedRecipe.toRemote() =
   FirebaseRecipe(
      recipe = recipe,
      userId = userId
   )