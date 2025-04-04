package com.ad.cookgood.search.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SearchField(
   modifier: Modifier = Modifier,
   textFieldState: TextFieldState = rememberTextFieldState(),
   searchBarState: SearchBarState = rememberSearchBarState(),
) {

   val scope = rememberCoroutineScope()

   SearchBarDefaults.InputField(
      modifier = modifier,
      searchBarState = searchBarState,
      textFieldState = textFieldState,
      onSearch = { scope.launch { searchBarState.animateToCollapsed() } },
      placeholder = { Text("Search...") },
      leadingIcon = {
         if (searchBarState.currentValue == SearchBarValue.Expanded) {
            IconButton(
               onClick = { scope.launch { searchBarState.animateToCollapsed() } }
            ) {
               Icon(
                  imageVector = Icons.AutoMirrored.Default.ArrowBack,
                  contentDescription = "Back"
               )
            }
         } else {
            Icon(Icons.Default.Search, contentDescription = null)
         }
      },
      trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
   )
}