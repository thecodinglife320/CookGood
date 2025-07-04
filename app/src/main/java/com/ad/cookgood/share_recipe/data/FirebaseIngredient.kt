package com.ad.cookgood.share_recipe.data

import com.ad.cookgood.recipes.domain.model.Ingredient
import com.google.firebase.firestore.DocumentId

data class FirebaseIngredient(
   @DocumentId val id: String? = null,
   val ingredient: Ingredient = Ingredient()
)