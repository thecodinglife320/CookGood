package com.ad.cookgood.navigation.data

import androidx.annotation.StringRes
import com.ad.cookgood.R

object MyCookBookScreen : NavDestination {
   override val route = "mycookbook"
   @StringRes
   override val title = R.string.mycookbook
}