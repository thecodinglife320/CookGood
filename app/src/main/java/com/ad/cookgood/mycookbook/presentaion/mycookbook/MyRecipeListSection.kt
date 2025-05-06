package com.ad.cookgood.mycookbook.presentaion.mycookbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ad.cookgood.R
import com.ad.cookgood.mycookbook.presentaion.state.MyRecipeUiState
import com.ad.cookgood.recipes.presentation.state.RecipeUiState
import com.ad.cookgood.shared.CoilImage

@Preview
@Composable
fun MyRecipeListSection(
   modifier: Modifier = Modifier,
   myRecipeUiStates: List<MyRecipeUiState> = listOf(
      MyRecipeUiState(recipeUiState = RecipeUiState("Chao ngao")),
      MyRecipeUiState(recipeUiState = RecipeUiState("Chao ngao")),
      MyRecipeUiState(recipeUiState = RecipeUiState("Chao ngao")),
   ),
   onMyRecipeClick: (Long) -> Unit = {}
) {
   Column(modifier) {
      Text(
         stringResource(R.string.myrecipes),
         modifier = Modifier.padding(
            start = dimensionResource(R.dimen.padding_medium),
            bottom = dimensionResource(R.dimen.padding_small)
         )
      )
      LazyHorizontalGrid(
         rows = GridCells.Fixed(2),
         contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_medium)),
         horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
         verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
         modifier = Modifier.height(168.dp)
      ) {
         items(
            items = myRecipeUiStates
         ) {
            RecipeCard(myRecipeUiState = it, onMyRecipeClick = onMyRecipeClick)
         }
      }
   }

}

@Preview
@Composable
fun RecipeCard(
   modifier: Modifier = Modifier,
   myRecipeUiState: MyRecipeUiState = MyRecipeUiState(0, RecipeUiState()),
   onMyRecipeClick: (Long) -> Unit = {},
) {
   Card(
      onClick = {
         onMyRecipeClick(myRecipeUiState.id)
      },
      modifier = modifier,
   ) {
      Row(
         verticalAlignment = Alignment.CenterVertically,
         modifier = Modifier.width(200.dp)
      ) {
         CoilImage(
            uri = myRecipeUiState.recipeUiState.uri,
            modifier = Modifier.size(80.dp)
         )
         Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
         Text(
            text = myRecipeUiState.recipeUiState.title,
            style = MaterialTheme.typography.titleMedium
         )
      }
   }
}