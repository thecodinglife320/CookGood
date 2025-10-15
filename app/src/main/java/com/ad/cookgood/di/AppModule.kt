package com.ad.cookgood.di

import android.content.Context
import com.ad.cookgood.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.appwrite.Client
import io.appwrite.services.Storage
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

   @Provides
   @Singleton
   fun provideAppWriteClient(@ApplicationContext applicationContext: Context): Client {
      return Client(applicationContext)
         .setEndpoint(BuildConfig.APPWRITE_ENDPOINT)
         .setProject(BuildConfig.APPWRITE_PROJECT_ID)
   }

   @Provides
   @Singleton
   fun provideAppWriteStorage(client: Client) = Storage(client)
}

