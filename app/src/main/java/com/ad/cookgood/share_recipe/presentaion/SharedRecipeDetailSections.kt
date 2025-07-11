package com.ad.cookgood.share_recipe.presentaion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ad.cookgood.R
import com.ad.cookgood.mycookbook.presentaion.myrecipedetail.InstructionRow
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.InstructionUiState

@Composable
fun BasicInfo(
   modifier: Modifier = Modifier,
   cookTime: String,
   servings: String
) {
   Row(
      horizontalArrangement = Arrangement.SpaceEvenly,
      modifier = modifier
   ) {
      InfoColumn(Icons.Default.AccessTime, cookTime)
      InfoColumn(Icons.Default.Face, servings)
   }
}

@Composable
fun InfoColumn(
   imageVector: ImageVector = Icons.Default.AccessTime,
   text: String = "1 tieng"
) {
   Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Icon(
         imageVector = imageVector,
         contentDescription = null,
         tint = MaterialTheme.colorScheme.onBackground
      )
      Text(text = text, color = MaterialTheme.colorScheme.onBackground)
   }
}

@Composable
fun Description(
   modifier: Modifier = Modifier,
   description: String = "abc"
) {
   Text(
      text = description,
      modifier = modifier,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onBackground,
      textAlign = TextAlign.Justify
   )
}

@Composable
fun IngredientCard(
   modifier: Modifier = Modifier,
   title: String = "Đường",
   //subtitle: String = "100g"
) {
   Card(
      modifier
         .width(200.dp)
         .padding(dimensionResource(R.dimen.padding_small)),
      elevation = CardDefaults.cardElevation(4.dp),
      shape = MaterialTheme.shapes.medium
   ) {
      Column(Modifier.padding(dimensionResource(R.dimen.padding_small))) {
         Text(text = title)
         Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))
//         Text(
//            text = subtitle,
//            style = MaterialTheme.typography.bodyMedium,
//            color = MaterialTheme.colorScheme.outline
//         )
      }
   }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SecondaryTextTabs(
   modifier: Modifier = Modifier,
   instructionUiStates: List<InstructionUiState>,
   ingredientUiStates: List<IngredientUiState>
) {
   var state by remember { mutableIntStateOf(0) }
   val titles = listOf(
      stringResource(R.string.nguyen_lieu),
      stringResource(R.string.cach_lam)
   )
   Column(modifier) {
      SecondaryTabRow(selectedTabIndex = state) {
         titles.forEachIndexed { index, title ->
            Tab(
               selected = state == index,
               onClick = { state = index },
               text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) }
            )
         }
      }
      when (state) {
         0 -> {
            if (ingredientUiStates.isNotEmpty()) {
               FlowRow(
                  horizontalArrangement = Arrangement.SpaceEvenly,
                  modifier = Modifier.fillMaxWidth()
               ) {
                  ingredientUiStates.forEach {
                     IngredientCard(
                        title = it.name
                     )
                  }
               }
            } else Text(
               "Chưa có nguyên liệu",
               modifier = Modifier.align(Alignment.CenterHorizontally),
               color = MaterialTheme.colorScheme.onBackground
            )
         }

         1 -> {
            if (instructionUiStates.isNotEmpty()) {

               InstructionRow(
                  instructionUiStates = instructionUiStates,
               )
            } else Text(
               "Chưa có cách làm",
               modifier = Modifier.align(Alignment.CenterHorizontally),
               color = MaterialTheme.colorScheme.onBackground
            )
         }
      }
      Spacer(Modifier.size(100.dp))
   }
}