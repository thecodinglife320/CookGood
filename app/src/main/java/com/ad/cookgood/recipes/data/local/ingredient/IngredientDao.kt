package com.ad.cookgood.recipes.data.local.ingredient

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IngredientDao {

   @Insert
   suspend fun insertIngredient(localIngredient: LocalIngredient): Long

   @Query("select * from ingredients where id = :ingredientId")
   suspend fun getIngredientById(ingredientId: Long): LocalIngredient?

   @Query("select * from ingredients")
   suspend fun getAllIngredient(): List<LocalIngredient>
}