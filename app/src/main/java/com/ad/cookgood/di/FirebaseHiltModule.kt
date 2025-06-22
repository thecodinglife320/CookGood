package com.ad.cookgood.di

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseHiltModule {

   @Provides
   fun auth() = Firebase.auth

   @Provides
   fun provideFireStore() = FirebaseFirestore.getInstance()
}