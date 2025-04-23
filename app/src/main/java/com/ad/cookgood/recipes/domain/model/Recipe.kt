package com.ad.cookgood.recipes.domain.model

import com.ad.cookgood.recipes.data.local.recipe.LocalRecipe

open class Recipe(
   open val title: String = "",
   open val brief: String = "",
   open val serving: Int = 0,
   open val cookTime: Int = 0,
)

fun Recipe.toLocal() =
   LocalRecipe(
      title = title,
      brief = brief,
      servings = serving,
      cookTime = cookTime
   )
