package com.ad.cookgood.navigation.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ad.cookgood.R
import com.ad.cookgood.authentication.presentation.AuthScreen
import com.ad.cookgood.authentication.presentation.AuthViewModel
import com.ad.cookgood.mycookbook.presentaion.mycookbook.MyCookBookScreen
import com.ad.cookgood.mycookbook.presentaion.mycookbook.MyCookBookViewModel
import com.ad.cookgood.mycookbook.presentaion.myrecipedetail.MyRecipeDetailScreen
import com.ad.cookgood.mycookbook.presentaion.myrecipedetail.MyRecipeViewModel
import com.ad.cookgood.mycookbook.presentaion.myrecipeedit.EditMyRecipeViewModel
import com.ad.cookgood.navigation.data.AuthScreen
import com.ad.cookgood.navigation.data.EditMyRecipeScreen
import com.ad.cookgood.navigation.data.MyCookBookScreen
import com.ad.cookgood.navigation.data.MyRecipeDetailScreen
import com.ad.cookgood.navigation.data.ProfileScreen
import com.ad.cookgood.navigation.data.RecipeEntryScreen
import com.ad.cookgood.navigation.data.SearchScreen
import com.ad.cookgood.profile.presentation.ProfileScreen
import com.ad.cookgood.profile.presentation.ProfileViewModel
import com.ad.cookgood.recipes.presentation.entry.RecipeEntryScreen
import com.ad.cookgood.recipes.presentation.entry.RecipeEntryViewModel
import com.ad.cookgood.search.presentation.SearchScreen

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
         val vm = hiltViewModel<AuthViewModel>()
         AuthScreen(
            authViewModel = vm,
            onSignInSuccess = { navController.navigate(SearchScreen.route) }
         )
      }

      //profileUiState screen
      composable(route = ProfileScreen.route) {
         val vm = hiltViewModel<ProfileViewModel>()
         ProfileScreen(
            profileViewModel = vm
         )
      }

      //search screen
      composable(route = SearchScreen.route) {
         val navBackStackEntry by navController.currentBackStackEntryAsState()
         val titleAppBar =
            if (navBackStackEntry?.destination?.route == SearchScreen.route) SearchScreen.title else R.string.empty
         SearchScreen(
            titleAppBar = titleAppBar,
            navigateToProfile = { navController.navigate(ProfileScreen.route) }
         )
      }

      //my cook book screen
      composable(route = MyCookBookScreen.route) {
         val navBackStackEntry by navController.currentBackStackEntryAsState()

         val titleAppBar =
            if (navBackStackEntry?.destination?.route == MyCookBookScreen.route) MyCookBookScreen.title else R.string.empty

         val vm: MyCookBookViewModel = hiltViewModel(it)

         MyCookBookScreen(
            titleAppBar = titleAppBar,
            navigateToRecipeEntryScreen = {
               navController.navigate(RecipeEntryScreen.route) {
                  launchSingleTop = true
               }
            },
            vm = vm,
            onMyRecipeClick = { recipeId ->
               navController.navigate(
                  MyRecipeDetailScreen.route.replace(
                     "{${MyRecipeDetailScreen.recipeIdArg}}",
                     "$recipeId"
                  )
               )
            },
            navigateToProfile = { navController.navigate(ProfileScreen.route) }
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

         val vm = hiltViewModel<MyRecipeViewModel>(it)

         MyRecipeDetailScreen(
            vm = vm,
            navigateUp = { navController.navigateUp() },
            navigateBack = { navController.popBackStack() },
            navigateToEditScreen = {
               val route =
                  EditMyRecipeScreen.route.replace("{${MyRecipeDetailScreen.recipeIdArg}}", "$it")
               navController.navigate(route)
            },
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

         val vm = hiltViewModel<EditMyRecipeViewModel>()

         RecipeEntryScreen(
            vm = vm,
            navigateBack = { navController.popBackStack() },
            navigateUp = { navController.navigateUp() }
         )
      }

      //recipe entry screen
      composable(route = RecipeEntryScreen.route) {

         val vm: RecipeEntryViewModel = hiltViewModel(it)
         RecipeEntryScreen(
            navigateUp = { navController.navigateUp() },
            navigateBack = { navController.popBackStack() },
            vm = vm,
         )
      }
   }
}