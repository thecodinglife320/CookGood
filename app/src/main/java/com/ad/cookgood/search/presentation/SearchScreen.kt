package com.ad.cookgood.search.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ad.cookgood.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
   modifier: Modifier = Modifier,
   titleAppBar: Int = R.string.search,
   navigateToProfile: () -> Unit
) {

   Scaffold(
      modifier,
      topBar = {
         SearchScreenAppBar(titleAppBar = titleAppBar, navigateToProfile = navigateToProfile)
      }
   ) {
      Text(stringResource(titleAppBar), modifier = Modifier.padding(it))
   }
}