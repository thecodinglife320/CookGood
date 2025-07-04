package com.ad.cookgood.like_recipe.data

import com.google.firebase.firestore.DocumentId

data class FirebaseFavoriteRecipe(
   @DocumentId val recipeId: String = "",
   val favoriteAt: Long = System.currentTimeMillis(),
)