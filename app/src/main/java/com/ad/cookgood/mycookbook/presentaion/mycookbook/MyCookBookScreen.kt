package com.ad.cookgood.mycookbook.presentaion.mycookbook

import android.annotation.SuppressLint
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.cookgood.R
import com.ad.cookgood.mycookbook.presentaion.state.MyCookBookUiState
import com.ad.cookgood.mycookbook.presentaion.state.MyRecipeUiState
import com.ad.cookgood.recipes.presentation.state.RecipeUiState

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
            onClick = navigateToRecipeEntryScreen
         ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
         }
      }
   ) {
      val myCookBookUiState by vm.myCookBookUiState.collectAsState()
      MyCookBookScreenContent(
         Modifier.padding(it),
         myCookBookUiState,
         onMyRecipeClick
      )
   }
}

@Preview
@Composable
fun MyCookBookScreenContent(
   modifier: Modifier = Modifier,
   myCookBookUiState: MyCookBookUiState = MyCookBookUiState(
      listOf(
         MyRecipeUiState(recipeUiState = RecipeUiState("Chao ngao")),
         MyRecipeUiState(recipeUiState = RecipeUiState("Chao ngao")),
         MyRecipeUiState(recipeUiState = RecipeUiState("Chao ngao")),
      )
   ),
   onMyRecipeClick: (Long) -> Unit = {},
) {
   Column(modifier) {
      MyRecipeListSection(
         Modifier.padding(top = dimensionResource(R.dimen.padding_medium)),
         myRecipeUiStates = myCookBookUiState.myRecipeUiStates,
         onMyRecipeClick = onMyRecipeClick,
      )
   }
}


