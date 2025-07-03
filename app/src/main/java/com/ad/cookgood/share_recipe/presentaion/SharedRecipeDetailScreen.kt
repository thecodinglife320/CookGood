package com.ad.cookgood.share_recipe.presentaion

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.cookgood.share_recipe.domain.model.SharedRecipeDetails

@Composable
fun SharedRecipeDetailScreen() {

   val vm = hiltViewModel<SharedRecipeViewModel>()
   val sharedRecipeDetails by vm.sharedRecipeDetails

   Text("Shared Recipe Detail Screen")
   SharedRecipeDetailScreenContent(
      onFavoriteClick = vm::addFavorite,
      sharedRecipeDetails = sharedRecipeDetails
   )
}

@Composable
fun SharedRecipeDetailScreenContent(
   modifier: Modifier = Modifier,
   onFavoriteClick: () -> Unit = {},
   sharedRecipeDetails: SharedRecipeDetails? = null
) {
   Column(modifier = modifier) {
      IconButton(onClick = onFavoriteClick) {
         Icon(
            Icons.Default.Favorite,
            contentDescription = null
         )
      }
      Text(sharedRecipeDetails.toString())
   }
}