package com.ad.cookgood.share_recipe.data

import android.util.Log
import com.ad.cookgood.share_recipe.domain.ShareRecipeRepository
import com.ad.cookgood.share_recipe.domain.model.SharedIngredient
import com.ad.cookgood.share_recipe.domain.model.SharedInstruction
import com.ad.cookgood.share_recipe.domain.model.SharedRecipe
import com.ad.cookgood.share_recipe.domain.model.SharedRecipeDetails
import com.ad.cookgood.share_recipe.domain.model.toRemote
import com.ad.cookgood.util.normalizeStringForSearch
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShareRecipeRepositoryImpl @Inject constructor(
   private val db: FirebaseFirestore
) : ShareRecipeRepository {

   override fun shareRecipe(
      sharedRecipe: SharedRecipe,
      sharedInstructions: List<SharedInstruction>,
      sharedIngredients: List<SharedIngredient>
   ) {
      val recipeRef = db.collection("recipes").document()
      recipeRef.set(sharedRecipe.toRemote())
         .addOnSuccessListener {
            Log.d(TAG, "Recipe added with ID: ${recipeRef.id}")

            val batch = db.batch()

            sharedInstructions.forEach { instruction ->
               val instructionRef = recipeRef.collection("instructions").document()
               batch.set(instructionRef, instruction.toRemote())
            }

            sharedIngredients.forEach { ingredient ->
               val ingredientRef = recipeRef.collection("ingredients").document()
               batch.set(ingredientRef, ingredient.toRemote())
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

   override suspend fun getSharedRecipe(sharedRecipeId: String) =
      try {
         val recipeDoc = db.collection("recipes").document(sharedRecipeId).get().await()
         val firebaseRecipe = recipeDoc.toObject(FirebaseRecipe::class.java) ?: return null

         val ingredientsSnapshot = db.collection("recipes").document(sharedRecipeId)
            .collection("ingredients").get().await()
         val firebaseIngredients =
            ingredientsSnapshot.documents.mapNotNull { it.toObject(FirebaseIngredient::class.java) }

         val stepsSnapshot = db.collection("recipes").document(sharedRecipeId)
            .collection("instructions").get().await()
         val firebaseInstructions =
            stepsSnapshot.documents.mapNotNull { it.toObject(FirebaseInstruction::class.java) }

         SharedRecipeDetails(
            firebaseRecipe.toSharedRecipe(),
            firebaseIngredients.map { it.toSharedIngredient() },
            firebaseInstructions.map { it.toSharedInstruction() }.sortedBy {
               it.instruction.stepNumber
            }).also {
            Log.d(TAG, "SharedRecipeDetails: $it")
         }
      } catch (e: Exception) {
         Log.e(TAG, "Error getting shared recipe", e)
         null
      }

   override fun getSharedMyRecipes(userId: String): Flow<List<SharedRecipe>> = callbackFlow {
      val query = db.collection("recipes")
         .whereEqualTo("userId", userId)

      val subscription = query.addSnapshotListener { value, error ->
         if (error != null) {
            close(error)
            return@addSnapshotListener
         }
         val recipes = value?.toObjects(FirebaseRecipe::class.java) ?: emptyList()
         trySend(recipes.map { it.toSharedRecipe() })
      }
      awaitClose { subscription.remove() }
   }

   override suspend fun searchRecipesByName(queryText: String): List<SharedRecipe> {
      val results = mutableListOf<FirebaseRecipe>()
      val normalizedQuery = normalizeStringForSearch(queryText)
      Log.d(TAG, "Normalized query: $normalizedQuery")
      try {
         // Ký tự cuối cùng trong dải Unicode để tạo ra giới hạn trên
         // Ví dụ: nếu prefix là "App", giới hạn trên sẽ là "App\uf8ff"
         // Điều này đảm bảo rằng tất cả các chuỗi bắt đầu bằng "App" sẽ được bao gồm.
         val endBoundary = normalizedQuery + "\uf8ff"

         val querySnapshot = db.collection("recipes")
            .orderBy("normalizedTitle") // Cần index cho trường này
            .whereGreaterThanOrEqualTo("normalizedTitle", normalizedQuery)
            .whereLessThanOrEqualTo("normalizedTitle", endBoundary)
            .get()
            .await()

         for (document in querySnapshot.documents) {
            document.toObject(FirebaseRecipe::class.java)?.let {
               results.add(it)
            }
         }
         Log.d(TAG, "Successfully fetched ${results.size} recipes starting with title: $queryText")
      } catch (e: Exception) {
         Log.e(TAG, "Error querying shared recipes starting with title", e)
      }
      return results.map { it.toSharedRecipe() }
   }

   override suspend fun getRecentSharedRecipes(limit: Int): List<SharedRecipe> {
      val results = mutableListOf<FirebaseRecipe>()
      try {
         Log.d("FirestoreQuery", "Fetching recent shared recipes with limit: $limit")
         val querySnapshot = db.collection("recipes")
            .orderBy("uploadAt", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .get()
            .await()

         for (document in querySnapshot.documents) {
            document.toObject(FirebaseRecipe::class.java)?.let { firebaseRecipe ->
               results.add(firebaseRecipe)
            }
         }
         Log.d(TAG, "Successfully fetched ${results.size} recent recipes.")
      } catch (e: Exception) {
         Log.e(TAG, "Error fetching recent shared recipes", e)
      }
      return results.map { it.toSharedRecipe() }
   }

   private companion object {
      const val TAG = "ShareRecipeRepositoryImpl"
   }
}

private fun FirebaseInstruction.toSharedInstruction() =
   SharedInstruction(
      id = id!!,
      instruction = instruction
   )

private fun FirebaseIngredient.toSharedIngredient() =
   SharedIngredient(
      id = id!!,
      ingredient = ingredient
   )

fun FirebaseRecipe.toSharedRecipe() = SharedRecipe(
   id = id!!,
   recipe = recipe,
   userId = userId!!,
   uploadAt = uploadAt,
)