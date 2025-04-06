package com.ad.cookgood

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ad.cookgood.search.presentation.SearchField

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CookGoodAppBar(
   modifier: Modifier = Modifier,
   titleAppBar: String = "Tim kiem",
   //canNavigateBack: Boolean = false,
   //navigateUp: () -> Unit = {},
   textFieldState: TextFieldState = rememberTextFieldState(),
   searchBarState: SearchBarState = rememberSearchBarState(),
) {
   Column(
      modifier = modifier,
      horizontalAlignment = Alignment.CenterHorizontally
   ) {

      TopAppBar(title = { Text(titleAppBar) })

      SearchBar(
         state = searchBarState,
         inputField = {
            SearchField(
               textFieldState = textFieldState,
               searchBarState = searchBarState
            )
         }
      )
      ExpandedFullScreenSearchBar(
         state = searchBarState,
         inputField = {
            SearchField(
               textFieldState = textFieldState,
               searchBarState = searchBarState
            )
         }
      ) {
      }
   }
}