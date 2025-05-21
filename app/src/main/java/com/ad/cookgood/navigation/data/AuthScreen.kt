package com.ad.cookgood.navigation.data

import com.ad.cookgood.R

object AuthScreen : NavDestination {
   override val route: String
      get() = "auth_screen"
   override val title: Int
      get() = R.string.auth_screen_title_bar
}

