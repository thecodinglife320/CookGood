package com.ad.cookgood.recipes.data.local.recipe

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

   @Insert
   suspend fun insertRecipe(localRecipe: LocalRecipe): Long

   @Query("select * from recipes where id = :recipeId")
   fun getRecipeById(recipeId: Long): Flow<LocalRecipe?>

   @Query("select * from recipes")
   fun getAllRecipe(): Flow<List<LocalRecipe>>

   @Delete
   suspend fun delete(localRecipe: LocalRecipe): Int

   @Update
   suspend fun update(localRecipe: LocalRecipe)
}