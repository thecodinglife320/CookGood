package com.ad.cookgood

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ad.cookgood.myrecipes.presentation.entry.RecipeEntryToolBar
import com.ad.cookgood.navigation.data.MyCookBookScreen
import com.ad.cookgood.navigation.data.RecipeEntryScreen
import com.ad.cookgood.navigation.data.SearchScreen
import com.ad.cookgood.navigation.presentation.BottomNavigationBar
import com.ad.cookgood.navigation.presentation.CookGoodNavHost
import com.ad.cookgood.ui.theme.CookGoodTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
   @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent {
         CookGoodTheme {

            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val titleAppBarRes = when (navBackStackEntry?.destination?.route) {
               SearchScreen.route -> SearchScreen.title
               MyCookBookScreen.route -> MyCookBookScreen.title
               RecipeEntryScreen.route -> RecipeEntryScreen.title
               else -> R.string.app_name
            }

            val isRecipeEntryScreen = when (navBackStackEntry?.destination?.route) {
               SearchScreen.route, MyCookBookScreen.route -> false
               else -> true
            }

            Scaffold(
               modifier = Modifier
                  .fillMaxSize()
                  .imePadding(),
               topBar = {
                  if (!isRecipeEntryScreen) {
                     CookGoodAppBar(
                        titleAppBar = stringResource(titleAppBarRes)
                     )
                  } else {
                     RecipeEntryToolBar(
                        navigateBack = { navController.popBackStack() },
                        navigateUp = {
                           navController.navigateUp()
                        },
                     )
                  }
               },
               bottomBar = {
                  if (!isRecipeEntryScreen) {
                     BottomNavigationBar(navController)
                  }
               },
               floatingActionButton = {
                  if (!isRecipeEntryScreen) {
                     FloatingActionButton(
                        onClick = {
                           navController.navigate(RecipeEntryScreen.route) {
                              launchSingleTop = true
                           }
                        },
                     ) {
                        Icon(
                           imageVector = Icons.Default.Add,
                           contentDescription = null
                        )
                     }
                  }
               },
            ) { paddingValue ->
               CookGoodNavHost(
                  navController,
                  paddingValues = paddingValue
               )
            }
         }
      }
   }
}
