package com.ad.cookgood.navigation.data

object SharedRecipeDetailScreen : NavDestination {

   @Suppress("ConstPropertyName")
   const val sharedRecipeIdArg = "shared_recipe_id"

   override val route = "SharedRecipeDetailScreen/{$sharedRecipeIdArg}"

}