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
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.ad.cookgood.R
import com.ad.cookgood.mycookbook.presentaion.state.SharedMyRecipeGridUiState
import com.ad.cookgood.share_recipe.data.FirebaseRecipe
import com.ad.cookgood.shared.CoilImage

@Composable
fun SharedMyRecipeGrid(
   modifier: Modifier = Modifier,
   sharedMyRecipeGridUiState: SharedMyRecipeGridUiState.Success,
   onSharedRecipeClick: (String?) -> Unit
) {
   Column(modifier) {
      Text(
         stringResource(R.string.sharedrecipes),
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
            items = sharedMyRecipeGridUiState.sharedMyRecipeUiStates
         ) {
            SharedRecipeCard(
               firebaseRecipe = it,
               onSharedRecipeClick = onSharedRecipeClick
            )
         }
      }
   }
}

@Composable
fun SharedRecipeCard(
   modifier: Modifier = Modifier,
   firebaseRecipe: FirebaseRecipe,
   onSharedRecipeClick: (String?) -> Unit = {},
) {
   Card(
      onClick = {
         onSharedRecipeClick(firebaseRecipe.id)
      },
      modifier = modifier,
   ) {
      Row(
         verticalAlignment = Alignment.CenterVertically,
         modifier = Modifier.width(200.dp)
      ) {
         CoilImage(
            uri = firebaseRecipe.recipe.uri?.toUri(),
            modifier = Modifier.size(80.dp)
         )
         Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
         Text(
            text = firebaseRecipe.recipe.title,
            style = MaterialTheme.typography.titleMedium
         )
      }
   }
}