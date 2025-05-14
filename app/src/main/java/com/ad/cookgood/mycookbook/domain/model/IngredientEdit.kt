package com.ad.cookgood.mycookbook.domain.model

import com.ad.cookgood.recipes.domain.model.Ingredient

data class IngredientEdit(
   val id: Long,
   val ingredient: Ingredient,
)