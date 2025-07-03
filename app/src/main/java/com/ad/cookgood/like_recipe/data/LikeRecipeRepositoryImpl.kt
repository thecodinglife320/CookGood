package com.ad.cookgood.like_recipe.data

import android.util.Log
import com.ad.cookgood.like_recipe.domain.LikeRecipeRepository
import com.ad.cookgood.like_recipe.domain.model.FavoriteRecipe
import com.ad.cookgood.like_recipe.domain.model.toData
import com.ad.cookgood.share_recipe.data.FirebaseRecipe
import com.ad.cookgood.share_recipe.data.toSharedRecipe
import com.ad.cookgood.share_recipe.domain.model.SharedRecipe
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LikeRecipeRepositoryImpl @Inject constructor(
   private val db: FirebaseFirestore,
) : LikeRecipeRepository {

   override suspend fun addFavorite(favoriteRecipe: FavoriteRecipe) {
      val firebaseFavoriteRecipe = favoriteRecipe.toData()

      val favoriteRef = db.collection("users").document(favoriteRecipe.userId)
         .collection("favorites")
         .document(firebaseFavoriteRecipe.recipeId)

      favoriteRef.set(firebaseFavoriteRecipe).await()
   }

   override fun getFavorites(userId: String): Flow<List<SharedRecipe>> = callbackFlow {

      val favoritesCollectionRef = db.collection("users").document(userId).collection("favorites")

      // Lắng nghe các thay đổi trong subcollection 'favorites' của người dùng
      val favoritesSubscription = favoritesCollectionRef.addSnapshotListener { snapshot, e ->
         if (e != null) {
            close(e)
            return@addSnapshotListener
         }

         if (snapshot != null) {
            val favoriteRecipeIds = snapshot.documents.mapNotNull { it.id }

            if (favoriteRecipeIds.isNotEmpty()) {
               launch { // Cần có CoroutineScope cho `launch`
                  val recipes = mutableListOf<FirebaseRecipe>()
                  // Lặp qua từng ID và fetch chi tiết công thức
                  for (id in favoriteRecipeIds) {
                     try {
                        val recipeDoc = db.collection("recipes").document(id).get().await()
                        recipeDoc.toObject(FirebaseRecipe::class.java)?.let {
                           recipes.add(it)
                        }
                     } catch (fetchError: Exception) {
                        Log.d(
                           TAG,
                           "Lỗi khi tải chi tiết công thức yêu thích $id: ${fetchError.message}"
                        )
                     }
                  }
                  // Sau khi đã fetch tất cả, phát ra danh sách
                  trySend(recipes.toList().map { it.toSharedRecipe() })
               }
            } else trySend(emptyList()) // Không có công thức yêu thích nào
         } else trySend(emptyList())
      }
      awaitClose { favoritesSubscription.remove() } // Hủy lắng nghe khi Flow kết thúc
   }

   private companion object {
      const val TAG = "LikeRecipeRepository"
   }

   private fun isRecipeFavorite(recipeId: String, currentUserId: String?): Flow<Boolean> =
      callbackFlow {

         val favoriteRef = db.collection("users").document(currentUserId ?: "")
            .collection("favorites").document(recipeId)

         val subscription = favoriteRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
               close(e) // Đóng flow với lỗi
               return@addSnapshotListener
            }
            trySend(snapshot?.exists() == true)
         }
         awaitClose { subscription.remove() }
      }
}

data class FirebaseFavoriteRecipe(
   @DocumentId val recipeId: String = "",
   val favoriteAt: Long = System.currentTimeMillis(),
)