package com.ad.cookgood.navigation.data

import androidx.annotation.StringRes
import com.ad.cookgood.R

object SearchScreen : NavDestination {
   override val route = "search"
   @StringRes
   override val title = R.string.search
}

