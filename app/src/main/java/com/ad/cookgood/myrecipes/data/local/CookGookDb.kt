package com.ad.cookgood.myrecipes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
   entities = [LocalRecipe::class],
   version = 1,
   exportSchema = false
)
abstract class CookGookDb : RoomDatabase() {

   abstract val recipeDao: RecipeDao
}