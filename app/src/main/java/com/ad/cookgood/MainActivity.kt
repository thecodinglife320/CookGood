package com.ad.cookgood

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ad.cookgood.navigation.data.MyCookBookScreen
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
            val backStackEntry by navController.currentBackStackEntryAsState()

            val isTopLevelDestination = when (backStackEntry?.destination?.route) {
               SearchScreen.route, MyCookBookScreen.route -> true
               else -> false
            }

            Scaffold(
               Modifier.imePadding(),
               bottomBar = {
                  if (isTopLevelDestination) {
                     BottomNavigationBar(navController)
                  }
               },
            ) { paddingValue ->
               CookGoodNavHost(
                  modifier = Modifier.padding(paddingValue),
                  navController = navController
               )
            }
         }
      }
   }
}
