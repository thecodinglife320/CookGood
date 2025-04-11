package com.ad.cookgood.search.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ad.cookgood.R
import com.ad.cookgood.SearchScreenAppBar

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
   modifier: Modifier = Modifier,
   titleAppBar: Int = R.string.search,
) {

   Scaffold(
      modifier,
      topBar = {
         SearchScreenAppBar(titleAppBar = titleAppBar)
      }
   ) {
      Text(stringResource(titleAppBar), modifier = Modifier.padding(it))
   }
}