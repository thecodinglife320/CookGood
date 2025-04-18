package com.ad.cookgood.myrecipes.di

import com.ad.cookgood.myrecipes.data.local.RecipeRepositoryImpl
import com.ad.cookgood.myrecipes.domain.RecipeRepository
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
}