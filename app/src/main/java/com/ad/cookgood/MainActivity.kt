package com.ad.cookgood

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ad.cookgood.captureimage.presentation.CameraPreview
import com.ad.cookgood.captureimage.presentation.CameraViewModel
import com.ad.cookgood.ui.theme.CookGoodTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
   @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent {
         CookGoodTheme {

//            val navController = rememberNavController()
//            val backStackEntry by navController.currentBackStackEntryAsState()
//
//            val isTopLevelDestination = when (backStackEntry?.destination?.route) {
//               SearchScreen.route, MyCookBookScreen.route -> true
//               else -> false
//            }
//
//            Scaffold(
//               Modifier.imePadding(),
//               bottomBar = {
//                  if (isTopLevelDestination) {
//                     BottomNavigationBar(navController)
//                  }
//               },
//            ) { paddingValue ->
//               CookGoodNavHost(
//                  modifier = Modifier.padding(paddingValue),
//                  navController = navController
//               )
//            }

            val vm = hiltViewModel<CameraViewModel>()
            CameraPreview(
               Modifier.height(300.dp),
               stopCamera = vm::stopCamera,
               startCamera = { a, b ->
                  vm.startCamera(a, b)
               },
               takePhoto = { vm.takePhoto() },
               uri = vm.uri.value
            )
         }
      }
   }
}
