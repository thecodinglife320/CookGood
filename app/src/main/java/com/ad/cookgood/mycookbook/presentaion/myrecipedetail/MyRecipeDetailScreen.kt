package com.ad.cookgood.mycookbook.presentaion.myrecipedetail

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ad.cookgood.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRecipeDetailScreen(
   modifier: Modifier = Modifier,
   vm: MyRecipeViewModel,
   navigateUp: () -> Unit
) {

   val myRecipeUiState = vm.myRecipeDetailUiState.value

   Scaffold(
      modifier = modifier,
      topBar = {
         TopAppBar(
            modifier = modifier.heightIn(max = 65.dp),
            title = { Text(myRecipeUiState.recipeUiState.title) },
            actions = {},
            navigationIcon = {
               IconButton(
                  onClick = navigateUp
               ) {
                  Icon(
                     imageVector = Icons.Default.ArrowBackIosNew,
                     contentDescription = "Back"
                  )
               }
            },
         )
      }
   ) {
      Column(Modifier.padding(it)) {
         RecipeInfoCard(
            cookTimeMinutes = myRecipeUiState.recipeUiState.cookTimeMinutes,
            cookTimeHours = myRecipeUiState.recipeUiState.cookTimeHours,
            servings = myRecipeUiState.recipeUiState.servings,
            brief = myRecipeUiState.recipeUiState.brief
         )
      }
   }
}

@Preview
@Composable
fun RecipeInfoColumn(
   modifier: Modifier = Modifier,
   @StringRes title: Int = R.string.recipe_entry_serving_labelTruoc,
   info: String = "abc"
) {
   Column(modifier) {
      Text(
         text = stringResource(id = title),
         style = MaterialTheme.typography.titleMedium
      )
      Text(text = info, style = MaterialTheme.typography.bodyMedium)
   }
}

@Preview
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecipeInfoCard(
   modifier: Modifier = Modifier,
   cookTimeMinutes: String = "10 phut",
   cookTimeHours: String = "3 tieng",
   servings: String = "10 nguoi",
   brief: String = "Dac san tay bac"
) {
   val recipeInfoMap = mapOf(
      R.string.recipe_entry_info_label to brief,
      R.string.recipe_entry_cook_time_label to "$cookTimeHours $cookTimeMinutes",
      R.string.recipe_entry_serving_labelTruoc to servings,
   )
   Card(modifier) {
      Column {
         HorizontalDivider(thickness = dimensionResource(R.dimen.padding_small))
         recipeInfoMap.forEach { (title, info) ->
            RecipeInfoColumn(
               title = title,
               info = info,
               modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )
         }
      }
   }
}