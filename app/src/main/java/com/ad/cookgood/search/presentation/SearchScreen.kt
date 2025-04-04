package com.ad.cookgood.search.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Text
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.ad.cookgood.R

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
) {
   //content
   LazyColumn(
      verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
   ) {
      val list = List(100) { "Text $it" }
      items(count = list.size) {
         Text(
            text = list[it],
            modifier = Modifier
               .fillMaxWidth()
               .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
         )
      }
   }
}