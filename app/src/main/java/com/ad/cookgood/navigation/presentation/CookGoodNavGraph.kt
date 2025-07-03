package com.ad.cookgood.navigation.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ad.cookgood.authentication.presentation.AuthScreen
import com.ad.cookgood.mycookbook.presentaion.mycookbook.MyCookBookScreen

import com.ad.cookgood.mycookbook.presentaion.myrecipedetail.MyRecipeDetailScreen
import com.ad.cookgood.mycookbook.presentaion.myrecipeedit.EditMyRecipeViewModel
import com.ad.cookgood.navigation.data.AuthScreen
import com.ad.cookgood.navigation.data.EditMyRecipeScreen
import com.ad.cookgood.navigation.data.MyCookBookScreen
import com.ad.cookgood.navigation.data.MyRecipeDetailScreen
import com.ad.cookgood.navigation.data.ProfileScreen
import com.ad.cookgood.navigation.data.RecipeEntryScreen
import com.ad.cookgood.navigation.data.SearchScreen
import com.ad.cookgood.navigation.data.SessionManagementScreen
import com.ad.cookgood.navigation.data.SharedRecipeDetailScreen
import com.ad.cookgood.profile.presentation.ProfileScreen
import com.ad.cookgood.recipes.presentation.entry.RecipeEntryScreen
import com.ad.cookgood.recipes.presentation.entry.RecipeEntryViewModel
import com.ad.cookgood.search.presentation.SearchScreen
import com.ad.cookgood.session_management.presentation.SessionManagementScreen
import com.ad.cookgood.share_recipe.presentaion.SharedRecipeDetailScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookGoodNavHost(
   modifier: Modifier = Modifier,
   navController: NavHostController = rememberNavController(),
) {
   NavHost(
      navController = navController,
      startDestination = AuthScreen.route,
      modifier = modifier
   ) {

      //auth screen
      composable(route = AuthScreen.route) {
         AuthScreen(
            onSignInSuccess = { navController.navigate(SearchScreen.route) }
         )
      }

      //profileUiState screen
      composable(route = ProfileScreen.route) {
         ProfileScreen(
            navigateUp = { navController.navigateUp() }
         )
      }

      //search screen
      composable(route = SearchScreen.route) {
         SearchScreen()
      }

      //my cook book screen
      composable(route = MyCookBookScreen.route) {

         MyCookBookScreen(
            navigateToRecipeEntryScreen = {
               navController.navigate(RecipeEntryScreen.route) {
                  launchSingleTop = true
               }
            },
            onMyRecipeClick = { recipeId ->
               navController.navigate(
                  MyRecipeDetailScreen.route.replace(
                     "{${MyRecipeDetailScreen.recipeIdArg}}",
                     "$recipeId"
                  )
               )
            },
            onSharedRecipeClick = {
               navController.navigate(
                  SharedRecipeDetailScreen.route.replace(
                     "{${SharedRecipeDetailScreen.sharedRecipeIdArg}}",
                     "$it"
                  )
               )
            }
         )
      }

      //session management screen
      composable(route = SessionManagementScreen.route) {
         SessionManagementScreen(
            onSignOutSuccess = {
               navController.popBackStack(AuthScreen.route, inclusive = false)
            },
            navigateToProfileScree = { navController.navigate(ProfileScreen.route) }
         )
      }

      //my recipe detail screen
      composable(
         route = MyRecipeDetailScreen.route,
         arguments = listOf(
            navArgument(MyRecipeDetailScreen.recipeIdArg) {
               type = NavType.LongType
            }
         ),
      ) {

         MyRecipeDetailScreen(
            navigateUp = { navController.navigateUp() },
            navigateToEditScreen = {
               val route =
                  EditMyRecipeScreen.route.replace("{${MyRecipeDetailScreen.recipeIdArg}}", "$it")
               navController.navigate(route)
            },
            navigateBack = { navController.popBackStack() },
         )
      }

      //my recipe edit screen
      composable(
         route = EditMyRecipeScreen.route,
         arguments = listOf(
            navArgument(MyRecipeDetailScreen.recipeIdArg) {
               type = NavType.LongType
            }
         )
      ) {
         val vm = hiltViewModel<EditMyRecipeViewModel>(it)
         RecipeEntryScreen(
            vm = vm,
            navigateBack = { navController.popBackStack() },
            navigateUp = { navController.navigateUp() }
         )
      }

      //recipe entry screen
      composable(route = RecipeEntryScreen.route) {
         val vm = hiltViewModel<RecipeEntryViewModel>()
         RecipeEntryScreen(
            navigateUp = { navController.navigateUp() },
            navigateBack = { navController.popBackStack() },
            vm = vm
         )
      }

      //shared recipe detail screen
      composable(
         route = SharedRecipeDetailScreen.route,
         arguments = listOf(
            navArgument(SharedRecipeDetailScreen.sharedRecipeIdArg) {
               type = NavType.StringType
            }
         )
      ) {
         SharedRecipeDetailScreen()
      }
   }
}