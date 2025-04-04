package com.ad.cookgood.navigation.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ad.cookgood.mycookbook.presentaion.MyCookBookScreen
import com.ad.cookgood.navigation.data.MyCookBookScreen
import com.ad.cookgood.navigation.data.SearchScreen
import com.ad.cookgood.search.presentation.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookGoodMavHost(
   navController: NavHostController
) {
   NavHost(
      navController = navController,
      startDestination = SearchScreen.ROUTE
   ) {
      composable(route = SearchScreen.ROUTE) {
         SearchScreen()
      }
      composable(route = MyCookBookScreen.ROUTE) {
         MyCookBookScreen()
      }
   }
}