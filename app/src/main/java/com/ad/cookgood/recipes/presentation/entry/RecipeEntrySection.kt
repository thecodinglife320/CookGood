package com.ad.cookgood.recipes.presentation.entry

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ad.cookgood.R
import com.ad.cookgood.recipes.presentation.state.CommonUiState
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.InstructionUiState

@Preview
@Composable
fun RecipeEntrySection1(
   modifier: Modifier = Modifier,
   onTitleChange: (String) -> Unit = {},
   onBriefChange: (String) -> Unit = {},
   keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {

   Column(
      modifier
   ) {
      //nhap tieu de cong thuc
      RecipeEntryInput(
         label = R.string.recipe_entry_name_label,
         placeHolder = R.string.recipe_entry_name_placeholder,
         onValueChange = onTitleChange,
         keyboardOptions = keyboardOptions
      )

      //nhap mo ta ngan
      RecipeEntryInput(
         label = R.string.recipe_entry_info_label,
         placeHolder = R.string.recipe_entry_info_placeholder,
         modifier = Modifier.height(150.dp),
         onValueChange = onBriefChange,
         keyboardOptions = keyboardOptions
      )
   }

}

@Preview
@Composable
fun RecipeEntrySection2(
   modifier: Modifier = Modifier,
   onServingChange: (String) -> Unit = {},
   onHourChange: (String) -> Unit = {},
   onMinuteChange: (String) -> Unit = {},
   keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {

   Column(modifier) {

      //nhap khau phan
      Row(
         verticalAlignment = Alignment.CenterVertically
      ) {
         Text(
            stringResource(R.string.recipe_entry_serving_labelTruoc),
            Modifier.weight(1f)
         )

         RecipeEntryInput(
            modifier = Modifier.width(75.dp),
            label = R.string.recipe_entry_serving_labelSau,
            placeHolder = R.string.default00,
            onValueChange = onServingChange,
            keyboardOptions = keyboardOptions
         )
      }

      //nhap thoi gian nau
      Row(
         verticalAlignment = Alignment.CenterVertically
      ) {
         Text(
            stringResource(R.string.recipe_entry_cook_time_label),
            Modifier.weight(1f)
         )
         RecipeEntryInput(
            modifier = Modifier.width(75.dp),
            label = R.string.hour_label,
            placeHolder = R.string.default00,
            onValueChange = onHourChange,
            keyboardOptions = keyboardOptions
         )
         RecipeEntryInput(
            modifier = Modifier.width(75.dp),
            label = R.string.minute_label,
            placeHolder = R.string.default00,
            onValueChange = onMinuteChange,
            keyboardOptions = keyboardOptions
         )
      }
   }
}

@Preview
@Composable
fun RecipeEntrySection3(
   modifier: Modifier = Modifier,
   @StringRes textRes: Int = R.string.nguyen_lieu,
   @StringRes buttonTextRes: Int = R.string.them_nguyen_lieu,
   addCommonUiState: () -> Unit = {},
   removeCommonUiState: (CommonUiState) -> Unit = {},
   updateCommonUiState: (CommonUiState, String) -> Unit = { _, _ -> },
   commonUiStates: List<CommonUiState> = listOf<IngredientUiState>(
      IngredientUiState(
         id = 1,
         name = "hello",
      )
   ),
   @StringRes label: Int = R.string.ingredient_entry_label,
   @StringRes placeHolder: Int = R.string.ingredient_entry_place_holder,
) {
   Column(modifier) {

      Text(stringResource(textRes))

      commonUiStates.forEach { uiState ->
         key(uiState.id) {
            CommonEntry(
               onValueChange = { name ->
                  updateCommonUiState(uiState, name)
               },
               onRemove = { removeCommonUiState(uiState) },
               stepNumber = if (uiState is InstructionUiState) uiState.stepNumber else null,
               label = label,
               placeHolder = placeHolder
            )
         }
      }

      OutlinedButton(
         onClick = addCommonUiState
      ) {
         Icon(imageVector = Icons.Default.Add, contentDescription = null)
         Text(stringResource(buttonTextRes))
      }
   }
}

@Preview
@Composable
fun CommonEntry(
   modifier: Modifier = Modifier,
   onValueChange: (String) -> Unit = {},
   onRemove: () -> Unit = {},
   stepNumber: Int? = null,
   @StringRes label: Int = R.string.ingredient_entry_label,
   @StringRes placeHolder: Int = R.string.ingredient_entry_place_holder,
) {
   Row(
      modifier,
      verticalAlignment = Alignment.CenterVertically
   ) {
      stepNumber?.let {
         CircularText("$it")
      }
      RecipeEntryInput(
         onValueChange = onValueChange,
         label = label,
         placeHolder = placeHolder,
      )

      IconButton(onClick = onRemove) {
         Icon(Icons.Default.Delete, contentDescription = null)
      }
   }
}

@Preview
@Composable
fun RecipeEntryInput(
   modifier: Modifier = Modifier,
   @StringRes label: Int = R.string.recipe_entry_name_label,
   @StringRes placeHolder: Int = R.string.recipe_entry_name_placeholder,
   onValueChange: (String) -> Unit = {},
   keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
   var text by rememberSaveable { mutableStateOf("") }
   OutlinedTextField(
      value = text,
      onValueChange = {
         text = it
         onValueChange(it)
      },
      label = { Text(stringResource(label)) },
      placeholder = { Text(stringResource(placeHolder)) },
      modifier = modifier,
      keyboardOptions = keyboardOptions
   )
}

@Preview
@Composable
fun CircularText(text: String = "1") {
   Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
         .size(32.dp) // Kích thước vuông
         .clip(CircleShape) // Bo thành hình tròn
         .background(Color.DarkGray) // Màu nền
   ) {
      Text(
         text = text,
         color = Color.White
      )
   }
}
