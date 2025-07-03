package com.ad.cookgood.share_recipe.domain.model

data class SharedRecipeDetails(
   val sharedRecipe: SharedRecipe,
   val sharedIngredients: List<SharedIngredient>,
   val sharedInstructions: List<SharedInstruction>
)

