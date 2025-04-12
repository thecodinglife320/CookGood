package com.ad.cookgood.myrecipes.data.local

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface IngredientDao {

   @Insert
   suspend fun insertIngredient(localIngredient: LocalIngredient)

}