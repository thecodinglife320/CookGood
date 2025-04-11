package com.ad.cookgood.mycookbook.presentaion

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ad.cookgood.R
import com.ad.cookgood.SearchScreenAppBar

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCookBookScreen(
   modifier: Modifier = Modifier,
   titleAppBar: Int = R.string.mycookbook,
   navigateToRecipeEntryScreen: () -> Unit = {},
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
      Text(stringResource(titleAppBar), modifier = Modifier.padding(it))
   }
}
