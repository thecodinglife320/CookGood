package com.ad.cookgood.myrecipes.di

import android.content.Context
import androidx.room.Room
import com.ad.cookgood.myrecipes.data.local.CookGookDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

   @Provides
   @Singleton
   fun provideAppDatabase(@ApplicationContext context: Context) =
      Room.databaseBuilder(
         context = context.applicationContext,
         klass = CookGookDb::class.java,
         name = "cook_good_db"
      ).build()

   @Provides
   fun provideRecipeDao(database: CookGookDb) = database.recipeDao

   @Provides
   fun provideIngredientDao(cookGookDb: CookGookDb) = cookGookDb.ingredientDao

   @Provides
   fun provideInstructionDao(cookGookDb: CookGookDb) = cookGookDb.instructionDao
}