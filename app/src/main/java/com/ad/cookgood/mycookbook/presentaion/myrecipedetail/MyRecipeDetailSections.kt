package com.ad.cookgood.mycookbook.presentaion.myrecipedetail

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ad.cookgood.R
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.InstructionUiState
import com.ad.cookgood.shared.CoilImage

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

@Preview
@Composable
fun InstructionRow(
   modifier: Modifier = Modifier,
   instructionUiStates: List<InstructionUiState> = listOf(
      InstructionUiState(name = "alo", stepNumber = 1, uri = null),
      InstructionUiState(name = "alo", stepNumber = 2, uri = null),
      InstructionUiState(name = "alo", stepNumber = 3, uri = null)
   )
) {

   if (instructionUiStates.isNotEmpty()) {

      var index by remember { mutableIntStateOf(0) }

      Column(
         modifier = modifier
      ) {

         //button and image
         Row(
            verticalAlignment = Alignment.Companion.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
         ) {

            //previous button
            FilledIconButton(
               onClick = { index-- },
               enabled = index != 0,
               modifier = Modifier.weight(0.3f)
            ) {
               Icon(
                  imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos,
                  contentDescription = null
               )
            }

            Column(
               Modifier
                  .weight(2.4f)
            ) {
               //step number
               Text(stringResource(R.string.step_number, instructionUiStates[index].stepNumber))

               //image per step
               CoilImage(
                  uri = instructionUiStates[index].uri,
                  modifier = Modifier
                     .height(200.dp)
                     .fillMaxWidth()
               )

               //instruction text
               Text(
                  text = instructionUiStates[index].name,
                  textAlign = TextAlign.Center
               )
            }

            //next button
            FilledIconButton(
               onClick = { index++ },
               enabled = index != instructionUiStates.size - 1,
               modifier = Modifier.weight(0.3f)
            ) {
               Icon(
                  imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                  contentDescription = null
               )
            }
         }
      }
   }
}

@Preview
@Composable
fun IngredientColumn(
   modifier: Modifier = Modifier,
   ingredientUiStates: List<IngredientUiState> = listOf(
      IngredientUiState(name = "rau\ncai"),
      IngredientUiState(name = "rau cai"),
      IngredientUiState(name = "rau cai")
   )
) {
   if (ingredientUiStates.isNotEmpty()) {
      Column(modifier) {
         ingredientUiStates.forEach {
            Column(Modifier.width(IntrinsicSize.Max)) {
               Spacer(Modifier.size(8.dp))
               Text(it.name)
               HorizontalDivider(thickness = 2.dp)
            }
         }
      }
   }
}