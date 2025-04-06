package com.ad.cookgood

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ad.cookgood.navigation.data.MyCookBookScreen
import com.ad.cookgood.navigation.data.SearchScreen
import com.ad.cookgood.navigation.presentation.BottomNavigationBar
import com.ad.cookgood.navigation.presentation.CookGoodMavHost
import com.ad.cookgood.ui.theme.CookGoodTheme

class MainActivity : ComponentActivity() {
   @OptIn(ExperimentalMaterial3Api::class)
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent {
         CookGoodTheme {

            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()

            val titleAppBarRes = when (navBackStackEntry?.destination?.route) {
               SearchScreen.ROUTE -> SearchScreen.title
               MyCookBookScreen.ROUTE -> MyCookBookScreen.title
               else -> SearchScreen.title
            }

            Scaffold(
               modifier = Modifier.fillMaxSize(),
               topBar = {
                  CookGoodAppBar(
                     titleAppBar = stringResource(titleAppBarRes)
                  )
               },
               bottomBar = {
                  BottomNavigationBar(navController)
               }
            ) { paddingValue ->
               CookGoodMavHost(
                  navController,
                  paddingValues = paddingValue
               )
            }
         }
      }
   }
}
