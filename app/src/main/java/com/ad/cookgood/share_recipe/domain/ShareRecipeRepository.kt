package com.ad.cookgood.share_recipe.domain

import com.ad.cookgood.share_recipe.data.FirebaseIngredient
import com.ad.cookgood.share_recipe.data.FirebaseInstruction
import com.ad.cookgood.share_recipe.data.FirebaseRecipe

interface ShareRecipeRepository {
   fun shareRecipe(
      firebaseRecipe: FirebaseRecipe,
      firebaseInstructions: List<FirebaseInstruction>,
      firebaseIngredients: List<FirebaseIngredient>
   )
}