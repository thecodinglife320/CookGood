package com.ad.cookgood.profile.domain

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
   fun getCurrentUser(): Flow<FirebaseUser?>
   suspend fun updateUserProfile(name: String, url: String)
}

