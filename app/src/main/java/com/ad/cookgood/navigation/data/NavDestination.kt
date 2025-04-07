package com.ad.cookgood.navigation.data

import com.ad.cookgood.R

interface NavDestination {
   val route: String
   val title: Int
      get() = R.string.app_name
}