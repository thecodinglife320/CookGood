package com.ad.cookgood.share_recipe.data

import android.util.Log
import com.ad.cookgood.share_recipe.domain.ShareRecipeRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ShareRecipeRepositoryImpl @Inject constructor(
   private val db: FirebaseFirestore
) : ShareRecipeRepository {

   override fun shareRecipe(
      firebaseRecipe: FirebaseRecipe,
      firebaseInstructions: List<FirebaseInstruction>,
      firebaseIngredients: List<FirebaseIngredient>
   ) {
      val recipeRef = db.collection("recipes").document()
      recipeRef.set(firebaseRecipe)
         .addOnSuccessListener {
            Log.d(TAG, "Recipe added with ID: ${recipeRef.id}")

            val batch = db.batch()

            firebaseInstructions.forEach { instruction ->
               val instructionRef = recipeRef.collection("instructions").document()
               batch.set(instructionRef, instruction)
            }

            firebaseIngredients.forEach { ingredient ->
               val ingredientRef = recipeRef.collection("ingredients").document()
               batch.set(ingredientRef, ingredient)
            }

            batch.commit()
               .addOnSuccessListener {
                  Log.d(TAG, "Instructions and ingredients added successfully")
               }
               .addOnFailureListener { e ->
                  Log.w(TAG, "Error adding instructions and ingredients", e)
               }
         }.addOnFailureListener { e ->
            Log.w(TAG, "Error adding recipe", e)
         }
   }

   private companion object {
      const val TAG = "ShareRecipeRepositoryImpl"
   }

}