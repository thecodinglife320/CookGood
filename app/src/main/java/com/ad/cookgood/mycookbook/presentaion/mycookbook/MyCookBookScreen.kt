package com.ad.cookgood.mycookbook.presentaion.mycookbook

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
   onSharedRecipeClick: (String?) -> Unit,
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
      val favoriteRecipeGridUiState by vm.favoriteRecipeGridUiState.collectAsState()
      val currentUser by vm.currentUser
      MyCookBookScreenContent(
         Modifier.padding(it),
         myRecipeUiStates,
         sharedMyRecipeGridUiState,
         favoriteRecipeGridUiState,
         onMyRecipeClick,
         onSharedRecipeClick,
         currentUser?.isAnonymous
      )
   }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MyCookBookScreenContent(
   modifier: Modifier = Modifier,
   myRecipeUiStates: List<MyRecipeUiState> = listOf(),
   sharedMyRecipeGridUiState: SharedMyRecipeGridUiState,
   favoriteRecipeGridUiState: SharedMyRecipeGridUiState,
   onMyRecipeClick: (Long) -> Unit = {},
   onSharedRecipeClick: (String?) -> Unit,
   isAnonymous: Boolean? = null,
) {
   Column(modifier) {
      MyRecipeListSection(
         Modifier.padding(top = dimensionResource(R.dimen.padding_medium)),
         myRecipeUiStates = myRecipeUiStates,
         onMyRecipeClick = onMyRecipeClick,
      )

      Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))

      //cong thuc da chia se
      Text(
         stringResource(R.string.sharedrecipes),
         modifier = Modifier.padding(
            start = dimensionResource(R.dimen.padding_medium),
            bottom = dimensionResource(R.dimen.padding_small)
         )
      )
      SharedMyRecipeGrid(
         modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
         sharedMyRecipeGridUiState = sharedMyRecipeGridUiState,
         onSharedRecipeClick = onSharedRecipeClick,
         emptyMessage = stringResource(R.string.empty_shared_recipe),
         isAnonymous = isAnonymous,
         anonymousMessage = stringResource(R.string.not_anonymous_require)
      )

      Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))

      //cong thuc yeu thich
      Text(
         stringResource(R.string.favorites),
         modifier = Modifier.padding(
            start = dimensionResource(R.dimen.padding_medium),
            bottom = dimensionResource(R.dimen.padding_small)
         )
      )
      SharedMyRecipeGrid(
         modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
         sharedMyRecipeGridUiState = favoriteRecipeGridUiState,
         onSharedRecipeClick = onSharedRecipeClick,
         emptyMessage = stringResource(R.string.empty_favorite_recipe),
         isAnonymous = isAnonymous,
         anonymousMessage = stringResource(R.string.not_anonymous_require2),
      )
   }
}


