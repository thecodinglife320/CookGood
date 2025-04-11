package com.ad.cookgood.myrecipes.data.local

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface RecipeDao {

   @Insert
   suspend fun insertRecipe(localRecipe: LocalRecipe): Long
}