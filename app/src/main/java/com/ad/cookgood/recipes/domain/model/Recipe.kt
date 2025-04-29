package com.ad.cookgood.recipes.domain.model

import com.ad.cookgood.recipes.data.local.recipe.LocalRecipe

class Recipe(
   val title: String = "",
   val brief: String = "",
   val serving: Int = 0,
   val cookTime: Int = 0,
)

fun Recipe.toLocal() =
   LocalRecipe(
      title = title,
      brief = brief,
      servings = serving,
      cookTime = cookTime
   )
