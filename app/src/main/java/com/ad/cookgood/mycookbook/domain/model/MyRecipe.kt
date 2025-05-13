package com.ad.cookgood.mycookbook.domain.model

import com.ad.cookgood.recipes.data.local.recipe.LocalRecipe
import com.ad.cookgood.recipes.domain.model.Recipe

data class MyRecipe(
   val id: Long,
   val recipe: Recipe,
)

fun MyRecipe.toLocal() =
   LocalRecipe(
      id = id,
      title = recipe.title,
      brief = recipe.brief,
      servings = recipe.serving,
      cookTime = recipe.cookTime,
      uri = recipe.uri
   )