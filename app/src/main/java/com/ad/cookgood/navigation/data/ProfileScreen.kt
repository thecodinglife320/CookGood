package com.ad.cookgood.navigation.data

import com.ad.cookgood.R

object ProfileScreen : NavDestination {
   override val route: String
      get() = "profile_screen"
   override val title: Int
      get() = R.string.profile_screen_title_bar
}