package com.ad.cookgood.myrecipes.presentation.entry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RecipeEntryToolBar(
   modifier: Modifier = Modifier,
   navigateBack: () -> Unit = {},
   navigateUp: () -> Unit = {},

   ) {
   TopAppBar(
      modifier = modifier,
      navigationIcon = {
         IconButton(
            onClick = navigateUp
         ) {
            Icon(
               imageVector = Icons.Default.Close,
               contentDescription = "Close"
            )
         }
      },
      actions = {
         IconButton(
            onClick = {
               println("Save and exit")
               navigateBack()
            }
         ) {
            Icon(
               imageVector = Icons.Default.Save,
               contentDescription = "Save"
            )
         }
      },
      title = {},
   )
}