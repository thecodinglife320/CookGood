package com.ad.cookgood.mycookbook.presentaion.myrecipedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ad.cookgood.R
import com.ad.cookgood.recipes.presentation.state.IngredientUiState
import com.ad.cookgood.recipes.presentation.state.InstructionUiState
import com.ad.cookgood.shared.CoilImage
import com.ad.cookgood.ui.theme.LocalDimens

@Composable
fun BasicInfo(
   modifier: Modifier = Modifier,
   cookTime: String,
   serving: String
) {
   Row(
      horizontalArrangement = Arrangement.SpaceEvenly,
      modifier = modifier
   ) {
      InfoColumn(Icons.Default.AccessTime, cookTime)
      InfoColumn(Icons.Default.Face, serving)
   }
}

@Preview
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
                     val words = it.name.split(" ")
                     IngredientCard(
                        title = words[1],
                        subtitle = words[0]
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

               var index by remember { mutableIntStateOf(0) }

               Column {

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
                        Text(
                           stringResource(
                              R.string.step_number,
                              instructionUiStates[index].stepNumber
                           ),
                           color = MaterialTheme.colorScheme.onBackground
                        )

                        //image per step
                        CoilImage(
                           uri = instructionUiStates[index].uri,
                           modifier = Modifier
                              .height(200.dp)
                              .fillMaxWidth()
                        )

                        //instruction text
                        Text(
                           color = MaterialTheme.colorScheme.onBackground,
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

@Preview
@Composable
fun IngredientCard(
   modifier: Modifier = Modifier,
   title: String = "Đường",
   subtitle: String = "100g"
) {
   Card(
      modifier
         .width(200.dp)
         .padding(dimensionResource(R.dimen.padding_small)),
      elevation = CardDefaults.cardElevation(4.dp),
      shape = MaterialTheme.shapes.medium
   ) {
      Column(Modifier.padding(LocalDimens.current.smallPadding)) {
         Text(text = title)
         Spacer(Modifier.size(dimensionResource(R.dimen.padding_small)))
         Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
         )
      }
   }
}