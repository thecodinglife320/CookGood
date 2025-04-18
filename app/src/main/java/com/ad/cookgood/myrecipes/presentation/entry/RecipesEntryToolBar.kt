package com.ad.cookgood.myrecipes.presentation.entry

import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RecipeEntryToolBar(
   modifier: Modifier = Modifier,
   navigateUp: () -> Unit = {},
   saveRecipe: () -> Unit = {},
) {

   val keyboardController = LocalSoftwareKeyboardController.current

   TopAppBar(
      modifier = modifier.heightIn(max = 65.dp),
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
               keyboardController?.hide()
               saveRecipe()
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