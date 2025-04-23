package com.ad.cookgood.di

import com.ad.cookgood.mycookbook.data.MyCookBookRepositoryImpl
import com.ad.cookgood.mycookbook.domain.MyCookBookRepository
import com.ad.cookgood.recipes.data.local.RecipeRepositoryImpl
import com.ad.cookgood.recipes.domain.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

   @Binds
   abstract fun bindRecipeRepository(
      recipeRepositoryImpl: RecipeRepositoryImpl,
   ): RecipeRepository

   @Binds
   abstract fun bindMyCookBookRepository(
      myCookBookRepositoryImpl: MyCookBookRepositoryImpl,
   ): MyCookBookRepository
}