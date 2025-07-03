package com.ad.cookgood.mycookbook.presentaion.mycookbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
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
import com.ad.cookgood.share_recipe.domain.model.SharedRecipe
import com.ad.cookgood.shared.CoilImage

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SharedMyRecipeGrid(
   modifier: Modifier = Modifier,
   sharedMyRecipeGridUiState: SharedMyRecipeGridUiState,
   onSharedRecipeClick: (String?) -> Unit,
   emptyMessage: String = stringResource(R.string.empty_shared_recipe),
   isAnonymous: Boolean? = null,
   anonymousMessage: String = stringResource(R.string.not_anonymous_require)
) {
   Box(modifier) {
      if (isAnonymous == true) {
         Text(anonymousMessage)
      } else if (isAnonymous == false)
         when (sharedMyRecipeGridUiState) {
            is SharedMyRecipeGridUiState.Error -> {
               Text(sharedMyRecipeGridUiState.message)
            }

            SharedMyRecipeGridUiState.Loading -> LoadingIndicator(Modifier.align(Alignment.Center))

            is SharedMyRecipeGridUiState.Success -> {
               if (sharedMyRecipeGridUiState.sharedMyRecipeUiStates.isEmpty()) {
                  Text(emptyMessage)
               } else
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
                           sharedRecipe = it,
                           onSharedRecipeClick = onSharedRecipeClick
                        )
                     }
                  }
         }
      }
   }
}

@Composable
fun SharedRecipeCard(
   modifier: Modifier = Modifier,
   sharedRecipe: SharedRecipe,
   onSharedRecipeClick: (String?) -> Unit = {},
) {
   Card(
      onClick = {
         onSharedRecipeClick(sharedRecipe.id)
      },
      modifier = modifier,
   ) {
      Row(
         verticalAlignment = Alignment.CenterVertically,
         modifier = Modifier.width(200.dp)
      ) {
         CoilImage(
            uri = sharedRecipe.recipe.photo?.toUri(),
            modifier = Modifier.size(80.dp)
         )
         Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
         Text(
            text = sharedRecipe.recipe.title,
            style = MaterialTheme.typography.titleMedium
         )
      }
   }
}