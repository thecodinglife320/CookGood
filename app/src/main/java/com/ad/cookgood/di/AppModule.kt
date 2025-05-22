package com.ad.cookgood.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
   @Provides
   @Named("GoogleClientId")
   fun provideGoogleClientId() =
      "980760619934-onpras1tv3lcfkmirmfsk13oit2dref5.apps.googleusercontent.com"
}

