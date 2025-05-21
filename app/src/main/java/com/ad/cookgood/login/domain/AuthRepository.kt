package com.ad.cookgood.login.domain

import android.app.Activity
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
   suspend fun signInWithGoogle(context: Activity)
   suspend fun signOut()
   suspend fun createGuest()
   fun getCurrentUser(): FirebaseUser?
}