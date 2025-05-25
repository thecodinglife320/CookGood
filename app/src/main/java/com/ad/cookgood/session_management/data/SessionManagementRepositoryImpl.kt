package com.ad.cookgood.session_management.data

import com.ad.cookgood.session_management.domain.SessionManagementRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SessionManagementRepositoryImpl @Inject constructor(
   private val auth: FirebaseAuth
) : SessionManagementRepository {

   override fun signOut() {
      auth.signOut()
   }

}