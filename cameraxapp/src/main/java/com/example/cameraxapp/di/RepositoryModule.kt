package com.example.cameraxapp.di

import com.example.cameraxapp.data.CameraRepositoryImpl
import com.example.cameraxapp.domain.CameraRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

   @Binds
   @Singleton
   abstract fun bindCameraRepository(
      impl: CameraRepositoryImpl
   ): CameraRepository
}