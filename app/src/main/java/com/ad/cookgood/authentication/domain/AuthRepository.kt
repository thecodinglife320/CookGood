package com.ad.cookgood.authentication.domain

import android.app.Activity
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
   suspend fun signInWithGoogle(context: Activity): AuthResult
   suspend fun signOut()
   suspend fun createGuest()
   fun getCurrentUser(): FirebaseUser?
}

