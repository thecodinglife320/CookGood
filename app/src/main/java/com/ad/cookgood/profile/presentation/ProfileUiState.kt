package com.ad.cookgood.profile.presentation

import android.net.Uri

sealed class ProfileUiState {

   object Loading : ProfileUiState()

   data class Success(
      val userId: String,
      val isAnonymous: Boolean,
      val name: String?,
      val url: Uri?
   ) : ProfileUiState()

}
