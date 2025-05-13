package com.ad.cookgood.navigation.data

import com.ad.cookgood.R

object MyRecipeDetailScreen : NavDestination {

   @Suppress("ConstPropertyName")
   const val recipeIdArg = "my_recipe_id"

   override val route = "${MyCookBookScreen.route}/{$recipeIdArg}"

   override val title = R.string.empty

}

object EditMyRecipeScreen : NavDestination {
   override val route = "${MyRecipeDetailScreen.route}/edit"
   override val title = R.string.empty
}