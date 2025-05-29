package com.ad.cookgood.search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenAppBar(
   modifier: Modifier = Modifier,
   //canNavigateBack: Boolean = false,
   //navigateUp: () -> Unit = {},
   textFieldState: TextFieldState = rememberTextFieldState(),
   searchBarState: SearchBarState = rememberSearchBarState()
) {
   Column(
      modifier = modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
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