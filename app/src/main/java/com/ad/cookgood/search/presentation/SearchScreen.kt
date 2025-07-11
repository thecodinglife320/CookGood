package com.ad.cookgood.search.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.cookgood.R
import com.ad.cookgood.mycookbook.presentaion.mycookbook.SharedRecipeCard
import com.ad.cookgood.mycookbook.presentaion.state.SharedMyRecipeGridUiState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SearchScreen(onSharedRecipeClick: (String?) -> Unit) {

   var text by remember { mutableStateOf("") }
   var active by remember { mutableStateOf(false) }
   var items by remember {
      mutableStateOf(
         mutableListOf<String>()
      )
   }
   val vm = hiltViewModel<SearchViewModel>()
   val sharedMyRecipeGridUiState = vm.searchedSharedRecipes.collectAsState().value
   val recentSharedRecipes = vm.recentSharedRecipes.collectAsState().value
   Scaffold(
      topBar = {
         SearchBar(
            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            query = text,
            onQueryChange = { text = it },
            onSearch = {
               active = false
               items.add(text)
               vm.onSearch(text)
            },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text(stringResource(R.string.search_hint)) },
            leadingIcon = {
               if (active) {
                  IconButton(onClick = {
                     active = false
                  }) {
                     Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back"
                     )
                  }
               } else {
                  IconButton({
                     active = true
                  }) {
                     Icon(Icons.Default.Search, contentDescription = null)
                  }
               }
            },
            trailingIcon = {
               if (active) {
                  IconButton(
                     onClick = {
                        if (text.isNotEmpty()) text = ""
                        else active = false
                     }
                  ) {
                     Icon(Icons.Default.Close, contentDescription = null)
                  }
               }
            }
         ) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
               items.forEach {
                  Row(
                     modifier = Modifier
                        .padding(14.dp)
                        .clickable {
                           text = it
                           active = false
                           vm.onSearch(text)
                        }) {
                     Icon(
                        modifier = Modifier.padding(end = 10.dp),
                        imageVector = Icons.Default.History,
                        contentDescription = null
                     )
                     Text(it)
                  }
               }
            }
         }
      }
   ) {
      Column(
         Modifier
            .padding(it)
            .padding(dimensionResource(R.dimen.padding_medium))
            .verticalScroll(rememberScrollState())
      ) {

         Text(
            stringResource(R.string.search_result),
            style = MaterialTheme.typography.titleLarge
         )

         when (sharedMyRecipeGridUiState) {
            is SharedMyRecipeGridUiState.Error -> ""
            SharedMyRecipeGridUiState.Loading -> LoadingIndicator()
            is SharedMyRecipeGridUiState.Success -> {
               if (sharedMyRecipeGridUiState.sharedMyRecipeUiStates.isEmpty()) {
                  Text(stringResource(R.string.no_result))
               } else {
                  sharedMyRecipeGridUiState.sharedMyRecipeUiStates.forEach { e ->
                     SharedRecipeCard(
                        sharedRecipe = e,
                        onSharedRecipeClick = onSharedRecipeClick,
                        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium))
                     )
                  }
               }
            }
         }

         Spacer(Modifier.size(dimensionResource(R.dimen.padding_medium)))

         Text(
            stringResource(R.string.recent_shared_recipe),
            style = MaterialTheme.typography.titleLarge
         )

         when (recentSharedRecipes) {
            is SharedMyRecipeGridUiState.Error -> ""
            SharedMyRecipeGridUiState.Loading -> LoadingIndicator()
            is SharedMyRecipeGridUiState.Success -> {
               if (recentSharedRecipes.sharedMyRecipeUiStates.isEmpty()) {
                  Text(stringResource(R.string.no_result))
               } else {
                  recentSharedRecipes.sharedMyRecipeUiStates.forEach { e ->
                     SharedRecipeCard(
                        sharedRecipe = e,
                        onSharedRecipeClick = onSharedRecipeClick,
                        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium))
                     )
                  }
               }
            }
         }
      }
   }
}