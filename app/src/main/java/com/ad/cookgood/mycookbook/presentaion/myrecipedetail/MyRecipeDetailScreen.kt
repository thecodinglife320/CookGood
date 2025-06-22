package com.ad.cookgood.mycookbook.presentaion.myrecipedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.cookgood.ui.theme.LocalDimens
import com.ad.cookgood.util.statusBarInsetDp

@Composable
fun MyRecipeDetailScreen(
   modifier: Modifier = Modifier,
   navigateUp: () -> Unit,
   navigateToEditScreen: (Long) -> Unit
) {

   val vm = hiltViewModel<MyRecipeViewModel>()
   val myRecipeUiState by vm.myRecipeUiState.collectAsState()
   val instructionUiStates by vm.instructionUiStates.collectAsState()
   val ingredientUiStates by vm.ingredientUiStates.collectAsState()
   val currentUser by vm.currentUser.collectAsState()

   val scrollState = rememberScrollState()

   val headerHeightPx = with(receiver = LocalDensity.current) {
      LocalDimens.current.headerHeight.toPx()
   }
   val toolbarHeightPx = with(receiver = LocalDensity.current) {
      LocalDimens.current.toolbarHeight.toPx()
   }

   val toolbarBottom = headerHeightPx - toolbarHeightPx
   val showToolbar by remember {
      derivedStateOf { scrollState.value >= toolbarBottom }
   }

   Box(
      modifier = modifier
         .fillMaxSize()
         .background(MaterialTheme.colorScheme.background)
   ) {
      myRecipeUiState?.let {
         Header(
            uri = it.recipeUiState.uri,
            headerHeightPx = headerHeightPx,
            scrollState = scrollState,
         )

         MyRecipeDetailScreenContent(
            scrollState = scrollState,
            description = it.recipeUiState.brief,
            serving = it.recipeUiState.servings,
            cookTime = "${it.recipeUiState.cookTimeHours} ${it.recipeUiState.cookTimeMinutes}",
            instructionUiStates = instructionUiStates,
            ingredientUiStates = ingredientUiStates,
         )

         Toolbar(
            showToolBar = showToolbar,
            navigateUp = navigateUp,
            navigateToEditScreen = {
               navigateToEditScreen(it.id)
            },
            deleteMyRecipe = { vm.deleteMyRecipe() }
         )

         Title(
            title = it.recipeUiState.title,
            headerHeightPx = headerHeightPx,
            toolbarHeightPx = toolbarHeightPx,
            scrollState = scrollState,
            showToolBar = showToolbar
         )

         Row(
            Modifier
               .fillMaxWidth()
               .align(Alignment.TopCenter)
               .padding(top = statusBarInsetDp)
         ) {
            FilledIconButton(onClick = navigateUp) {
               Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = null)
            }
            Spacer(Modifier.weight(1f))
            FilledIconButton(onClick = { vm.deleteMyRecipe() }) {
               Icon(Icons.Default.Delete, contentDescription = null)
            }
            FilledIconButton(onClick = {
               navigateToEditScreen(it.id)
            }) {
               Icon(Icons.Default.Edit, contentDescription = null)
            }
            currentUser?.let {
               if (it.isAnonymous.not()) {
                  FilledIconButton(onClick = { vm.shareRecipe() }) {
                     Icon(Icons.Default.Share, contentDescription = null)
                  }
               }
            }
         }
      }
   }
}