package com.ad.cookgood.mycookbook.presentaion.mycookbook

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.cookgood.R
import com.ad.cookgood.mycookbook.presentaion.state.MyRecipeUiState
import com.ad.cookgood.mycookbook.presentaion.state.SharedMyRecipeGridUiState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyCookBookScreen(
   navigateToRecipeEntryScreen: () -> Unit = {},
   onMyRecipeClick: (Long) -> Unit = {},
) {

   val vm: MyCookBookViewModel = hiltViewModel()

   Scaffold(
      floatingActionButton = {
         FloatingActionButton(
            shape = CircleShape,
            onClick = navigateToRecipeEntryScreen
         ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
         }
      }
   ) {
      val myRecipeUiStates by vm.myRecipeUiStates.collectAsState()
      val sharedMyRecipeGridUiState by vm.sharedMyRecipeGridUiState.collectAsState()
      MyCookBookScreenContent(
         Modifier.padding(it),
         myRecipeUiStates,
         sharedMyRecipeGridUiState,
         onMyRecipeClick
      )
   }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyCookBookScreenContent(
   modifier: Modifier = Modifier,
   myRecipeUiStates: List<MyRecipeUiState> = listOf(),
   sharedMyRecipeGridUiState: SharedMyRecipeGridUiState,
   onMyRecipeClick: (Long) -> Unit = {},
) {
   Column(modifier) {
      MyRecipeListSection(
         Modifier.padding(top = dimensionResource(R.dimen.padding_medium)),
         myRecipeUiStates = myRecipeUiStates,
         onMyRecipeClick = onMyRecipeClick,
      )
      when (sharedMyRecipeGridUiState) {
         is SharedMyRecipeGridUiState.Error -> {
            Text(sharedMyRecipeGridUiState.message)
         }

         SharedMyRecipeGridUiState.Loading -> LoadingIndicator()

         is SharedMyRecipeGridUiState.Success -> {
            SharedMyRecipeGrid(
               sharedMyRecipeGridUiState = sharedMyRecipeGridUiState,
               onSharedRecipeClick = {}
            )
         }
      }
   }
}


