package com.ad.cookgood.session_management.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ad.cookgood.R

@Preview
@Composable
fun SignOutButton(
   onSignOutButtonClick: () -> Unit = {}
) {
   OutlinedButton(onClick = onSignOutButtonClick) {
      Text(stringResource(R.string.signout))
      Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))
      Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
   }
}