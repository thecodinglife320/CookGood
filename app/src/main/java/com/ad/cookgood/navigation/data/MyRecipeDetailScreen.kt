package com.ad.cookgood.navigation.data

import com.ad.cookgood.R

object MyRecipeDetailScreen : NavDestination {

   const val recipeIdArg = "my_recipe_id"

   override val route = "${MyCookBookScreen.route}/{$recipeIdArg}"

   override val title = R.string.empty

}