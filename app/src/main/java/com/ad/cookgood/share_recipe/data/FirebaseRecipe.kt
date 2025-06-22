package com.ad.cookgood.share_recipe.data

import com.ad.cookgood.recipes.domain.model.Recipe
import com.google.firebase.firestore.DocumentId

data class FirebaseRecipe(
   @DocumentId val id: String? = null,
   val recipe: Recipe = Recipe(),
   val userId: String? = null
)

