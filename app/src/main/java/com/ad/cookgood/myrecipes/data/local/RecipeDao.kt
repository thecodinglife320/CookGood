package com.ad.cookgood.myrecipes.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecipeDao {

   @Insert
   suspend fun insertRecipe(localRecipe: LocalRecipe): Long

   @Query("select * from recipes where id = :recipeId")
   suspend fun getRecipeById(recipeId: Long): LocalRecipe?
}