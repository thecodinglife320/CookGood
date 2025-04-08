package com.ad.cookgood.myrecipes.presentation.entry

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ad.cookgood.R

@Preview()
@Composable
fun RecipeEntryScreen(
   modifier: Modifier = Modifier,
) {

   val vm: RecipeEntryViewModel = viewModel()
   val recipeEntryUiState = vm.recipeEntryUiState.value

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
         modifier = modifier,
         value = recipeEntryUiState.name,
         onValueChange = { name ->
            vm.updateUiState(recipeEntryUiState.copy(name = name))
         },
         keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
         )
      )

      //Nhap thong tin
      RecipeEntryInput(
         label = R.string.recipe_entry_info_label,
         placeHolder = R.string.recipe_entry_info_placeholder,
         modifier = modifier.height(150.dp),
         value = recipeEntryUiState.brief,
         onValueChange = { brief ->
            vm.updateUiState(recipeEntryUiState.copy(brief = brief))
         },
         keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
         )
      )

      //nhap khau phan
      Row(
         modifier,
         verticalAlignment = Alignment.CenterVertically
      ) {
         Text(stringResource(R.string.recipe_entry_serving_labelTruoc))

         Spacer(modifier = Modifier.weight(1f))

         RecipeEntryInput(
            modifier = Modifier.width(100.dp),
            label = R.string.recipe_entry_serving_labelSau,
            placeHolder = R.string.default00,
            value = recipeEntryUiState.servings,
            onValueChange = {
               vm.updateUiState(recipeEntryUiState.copy(servings = it))
            },
            keyboardOptions = KeyboardOptions(
               keyboardType = KeyboardType.Number,
               imeAction = ImeAction.Next,
            )
         )
      }

      //nhap thoi gian nau
      Row(
         modifier,
         verticalAlignment = Alignment.CenterVertically
      ) {
         Text(stringResource(R.string.recipe_entry_cook_time_label))

         Spacer(modifier = Modifier.weight(1f))

         RecipeEntryInput(
            modifier = Modifier.width(100.dp),
            label = R.string.hour_label,
            placeHolder = R.string.default00,
            value = recipeEntryUiState.cookTimeHours,
            onValueChange = {
               vm.updateUiState(recipeEntryUiState.copy(cookTimeHours = it))
            },
            keyboardOptions = KeyboardOptions(
               keyboardType = KeyboardType.Number,
               imeAction = ImeAction.Next,
            )
         )

         RecipeEntryInput(
            modifier = Modifier.width(100.dp),
            label = R.string.minute_label,
            placeHolder = R.string.default00,
            value = recipeEntryUiState.cookTimeMinutes,
            onValueChange = {
               vm.updateUiState(recipeEntryUiState.copy(cookTimeMinutes = it))
            },
            keyboardOptions = KeyboardOptions(
               keyboardType = KeyboardType.Number,
               imeAction = ImeAction.Next,
            )
         )
      }
   }
}

@Preview
@Composable
fun RecipeEntryInput(
   modifier: Modifier = Modifier,
   @StringRes label: Int = R.string.recipe_entry_name_label,
   @StringRes placeHolder: Int = R.string.recipe_entry_name_placeholder,
   value: String = "",
   onValueChange: (String) -> Unit = {},
   keyboardOptions: KeyboardOptions = KeyboardOptions(),
) {
   OutlinedTextField(
      value = value,
      onValueChange = {
         onValueChange(it)
      },
      label = { Text(stringResource(label)) },
      placeholder = { Text(stringResource(placeHolder)) },
      modifier = modifier,
      keyboardOptions = keyboardOptions
   )
}