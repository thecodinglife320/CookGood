package com.ad.cookgood.mycookbook.presentaion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.cookgood.R
import com.ad.cookgood.SearchScreenAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MyCookBookScreen(
   modifier: Modifier = Modifier,
   titleAppBar: Int = R.string.mycookbook,
   navigateToRecipeEntryScreen: () -> Unit = {},
   vm: MyCookBookViewModel = hiltViewModel(),
) {

   Scaffold(
      modifier,
      topBar = {
         SearchScreenAppBar(titleAppBar = titleAppBar)
      },
      floatingActionButton = {
         FloatingActionButton(
            onClick = navigateToRecipeEntryScreen
         ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
         }
      }
   ) {
      if (vm.myCookBookUiState.value.isLoading) {
         CircularProgressIndicator()
      } else {
         Column(
            Modifier.padding(it)
         ) {
            MyRecipeListSection(
               Modifier.padding(top = dimensionResource(R.dimen.padding_medium)),
               myRecipeUiStates = vm.myCookBookUiState.value.myRecipeUiStates
            )
         }
      }
   }
}


