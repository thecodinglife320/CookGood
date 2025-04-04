package com.ad.cookgood.navigation.data

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.ad.cookgood.R

data class BottomNavItem(
   @StringRes val label: Int,
   val icon: ImageVector,
   val route: String,
)

val bottomNavItems = listOf(
   BottomNavItem(R.string.search , Icons.Filled.Search, SearchScreen.ROUTE),
   BottomNavItem(R.string.mycookbook, Icons.Filled.Face, MyCookBookScreen.ROUTE)
)