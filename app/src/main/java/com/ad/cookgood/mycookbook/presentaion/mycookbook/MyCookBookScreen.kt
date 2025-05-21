package com.ad.cookgood.mycookbook.presentaion.mycookbook

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.cookgood.R
import com.ad.cookgood.search.presentation.SearchScreenAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCookBookScreen(
   modifier: Modifier = Modifier,
   titleAppBar: Int = R.string.mycookbook,
   navigateToRecipeEntryScreen: () -> Unit = {},
   vm: MyCookBookViewModel = hiltViewModel(),
   onMyRecipeClick: (Long) -> Unit = {},
   navigateToProfile: () -> Unit
) {

   Scaffold(
      modifier,
      topBar = {
         SearchScreenAppBar(
            titleAppBar = titleAppBar,
            navigateToProfile = navigateToProfile
         )
      },
      floatingActionButton = {
         FloatingActionButton(
            onClick = navigateToRecipeEntryScreen
         ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
         }
      }
   ) {
      val myCookBookUiState by vm.myCookBookUiState.collectAsState()
         Column(
            Modifier.padding(it)
         ) {
            MyRecipeListSection(
               Modifier.padding(top = dimensionResource(R.dimen.padding_medium)),
               myRecipeUiStates = myCookBookUiState.myRecipeUiStates,
               onMyRecipeClick = onMyRecipeClick,
            )
         }
      }
}


