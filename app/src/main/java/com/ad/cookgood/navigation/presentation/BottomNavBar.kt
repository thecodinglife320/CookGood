package com.ad.cookgood.navigation.presentation

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ad.cookgood.navigation.data.bottomNavItems
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy

@Composable
fun BottomNavigationBar(navController: NavHostController) {
   NavigationBar {

      val navBackStackEntry by navController.currentBackStackEntryAsState()
      val currentDestination = navBackStackEntry?.destination

      bottomNavItems.forEach { item ->
         NavigationBarItem(
            icon = {
               Icon(
                  imageVector = item.icon,
                  contentDescription = stringResource(item.label)
               )
            },
            label = { Text(stringResource(item.label)) },
            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
            onClick = {
               navController.navigate(item.route) {
                  popUpTo(navController.graph.startDestinationId) {
                     saveState = true
                  }
                  launchSingleTop = true
                  restoreState = true
               }
            }
         )
      }
   }
}