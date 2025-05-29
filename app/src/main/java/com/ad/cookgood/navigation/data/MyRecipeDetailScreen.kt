package com.ad.cookgood.navigation.data

object MyRecipeDetailScreen : NavDestination {

   @Suppress("ConstPropertyName")
   const val recipeIdArg = "my_recipe_id"

   override val route = "${MyCookBookScreen.route}/{$recipeIdArg}"
}

