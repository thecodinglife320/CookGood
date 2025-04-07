package com.ad.cookgood.myrecipes.presentation.entry

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ad.cookgood.R

@Preview()
@Composable
fun RecipeEntryScreen(
   modifier: Modifier = Modifier,
) {
   Column(
      modifier = modifier
         .fillMaxSize()
         .verticalScroll(rememberScrollState()),
   ) {
      Box {
         Image(
            painter = painterResource(R.drawable.recipe_entry_image),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
         )
         Text(
            stringResource(R.string.add_image),
            Modifier.align(Alignment.BottomCenter),
            style = MaterialTheme.typography.titleLarge
         )
      }

      val modifier = Modifier
         .fillMaxWidth()
         .padding(
            start = dimensionResource(R.dimen.padding_small),
            end = dimensionResource(R.dimen.padding_small),
            top = dimensionResource(R.dimen.padding_small)
         )

      //Nhap ten mon
      RecipeEntryInput(
         label = R.string.recipe_entry_name_label,
         placeHolder = R.string.recipe_entry_name_placeholder,
         modifier = modifier
      )

      //Nhap thong tin
      RecipeEntryInput(
         label = R.string.recipe_entry_info_label,
         placeHolder = R.string.recipe_entry_info_placeholder,
         modifier = modifier.height(100.dp)
      )

   }
}

@Preview
@Composable
fun RecipeEntryInput(
   modifier: Modifier = Modifier,
   @StringRes label: Int = R.string.recipe_entry_name_label,
   @StringRes placeHolder: Int = R.string.recipe_entry_name_placeholder,
   //onValueChange:() -> Unit={}
) {
   OutlinedTextField(
      value = "",
      onValueChange = {},
      label = { Text(stringResource(label)) },
      placeholder = { Text(stringResource(placeHolder)) },
      modifier = modifier
   )
}

@Preview
@Composable
fun RecipeEntryInput2(
   modifier: Modifier = Modifier,
   @StringRes labelTruoc: Int = R.string.recipe_entry_serving_labelTruoc,
   @StringRes labelSau: Int = R.string.recipe_entry_serving_labelSau,
   recipeEntryUiState: RecipeEntryUiState = RecipeEntryUiState(),
   //onValueChange:() -> Unit={}
) {
   Row(modifier) {
      Text(stringResource(labelTruoc))
      TextField()
      Text(stringResource(labelSau))
   }
}