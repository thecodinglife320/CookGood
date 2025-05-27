package com.ad.cookgood.navigation.data

import com.ad.cookgood.R

object SessionManagementScreen : NavDestination {
   override val route: String
      get() = "session_management_screen"
   override val title: Int
      get() = R.string.session_management_screen_title_bar
}