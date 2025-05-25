package com.ad.cookgood.di

import com.ad.cookgood.authentication.data.AuthRepositoryImpl
import com.ad.cookgood.authentication.domain.AuthRepository
import com.ad.cookgood.captureimage.data.CameraRepositoryImpl
import com.ad.cookgood.captureimage.domain.CameraRepository
import com.ad.cookgood.mycookbook.data.MyRecipeRepositoryImpl
import com.ad.cookgood.mycookbook.domain.MyRecipeRepository
import com.ad.cookgood.profile.data.UserProfileRepositoryImpl
import com.ad.cookgood.profile.domain.UserProfileRepository
import com.ad.cookgood.recipes.data.local.RecipeRepositoryImpl
import com.ad.cookgood.recipes.domain.RecipeRepository
import com.ad.cookgood.session_management.data.SessionManagementRepositoryImpl
import com.ad.cookgood.session_management.domain.SessionManagementRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

   @Binds
   abstract fun bindRecipeRepository(
      recipeRepositoryImpl: RecipeRepositoryImpl,
   ): RecipeRepository

   @Binds
   abstract fun bindMyRecipeRepository(
      impl: MyRecipeRepositoryImpl
   ): MyRecipeRepository

   @Singleton
   @Binds
   abstract fun bindCameraRepository(
      impl: CameraRepositoryImpl
   ): CameraRepository

   @Singleton
   @Binds
   abstract fun bindAuthRepository(
      impl: AuthRepositoryImpl
   ): AuthRepository

   @Singleton
   @Binds
   abstract fun bindSessionManagementRepo(
      impl: SessionManagementRepositoryImpl
   ): SessionManagementRepository

   @Singleton
   @Binds
   abstract fun bindUserProfileRepository(
      impl: UserProfileRepositoryImpl
   ): UserProfileRepository
}