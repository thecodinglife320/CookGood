package com.ad.cookgood.navigation.data

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.ad.cookgood.R

data class BottomNavItem(
   @StringRes val label: Int,
   val icon: ImageVector,
   val route: String,
)

val bottomNavItems = listOf(
   BottomNavItem(R.string.search, Icons.Filled.Search, SearchScreen.route),
   BottomNavItem(R.string.mycookbook, Icons.AutoMirrored.Filled.MenuBook, MyCookBookScreen.route),
   BottomNavItem(R.string.account, Icons.Filled.AccountCircle, SessionManagementScreen.route)
)