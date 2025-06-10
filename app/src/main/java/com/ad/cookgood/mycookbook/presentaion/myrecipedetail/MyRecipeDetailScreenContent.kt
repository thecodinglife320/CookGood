package com.ad.cookgood.mycookbook.presentaion.myrecipedetail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ad.cookgood.R
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.InstructionUiState
import com.ad.cookgood.ui.theme.LocalDimens

@Composable
fun MyRecipeDetailScreenContent(
   modifier: Modifier = Modifier,
   scrollState: ScrollState,
   description: String,
   serving: String,
   cookTime: String,
   instructionUiStates: List<InstructionUiState>,
   ingredientUiStates: List<IngredientUiState>
) {
   Column(
      modifier
         .verticalScroll(scrollState)

   ) {
      Spacer(
         Modifier
            .padding(top = LocalDimens.current.headerHeight)
      )

      Column(
         Modifier.background(MaterialTheme.colorScheme.background)
      ) {

         BasicInfo(
            modifier = Modifier
               .padding(LocalDimens.current.doubleContentPadding)
               .fillMaxWidth(),
            cookTime = cookTime,
            serving = serving
         )

         Description(
            description = description,
            modifier = Modifier.padding(LocalDimens.current.doubleContentPadding)
         )

         SecondaryTextTabs(
            instructionUiStates = instructionUiStates,
            ingredientUiStates = ingredientUiStates
         )
      }
   }
}

@Preview
@Composable
fun DeleteConfirmationDialog(
   modifier: Modifier = Modifier,
   onDeleteConfirm: () -> Unit = {},
   onDeleteCancel: () -> Unit = {},
) {
   AlertDialog(
      onDismissRequest = { /* Do nothing */ },
      title = { Text(stringResource(R.string.attention)) },
      text = { Text(stringResource(R.string.delete_question)) },
      modifier = modifier,
      dismissButton = {
         TextButton(onClick = onDeleteCancel) {
            Text(stringResource(R.string.no))
         }
      },
      confirmButton = {
         TextButton(onClick = onDeleteConfirm) {
            Text(stringResource(R.string.yes))
         }
      })
}