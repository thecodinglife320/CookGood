package com.ad.cookgood.profile.presentation

import android.net.Uri

sealed class ProfileUiState {

   object Loading : ProfileUiState()

   data class Success(
      val email: String?,
      val name: String?,
      val url: Uri?
   ) : ProfileUiState()

}
