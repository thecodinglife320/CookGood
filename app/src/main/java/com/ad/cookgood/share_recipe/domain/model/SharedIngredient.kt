package com.ad.cookgood.share_recipe.domain.model

import com.ad.cookgood.recipes.domain.model.Ingredient
import com.ad.cookgood.share_recipe.data.FirebaseIngredient

data class SharedIngredient(
   val id: String,
   val ingredient: Ingredient,
)

fun SharedIngredient.toRemote() =
   FirebaseIngredient(
      ingredient = ingredient
   )

