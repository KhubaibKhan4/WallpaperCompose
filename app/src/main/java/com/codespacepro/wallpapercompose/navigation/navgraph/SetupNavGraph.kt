package com.codespacepro.wallpapercompose.navigation.navgraph

import AboutUsScreen
import FavouriteScreen
import com.codespacepro.wallpapercompose.screen.FeedbackScreen
import com.codespacepro.wallpapercompose.screen.FullScreen
import com.codespacepro.wallpapercompose.screen.SettingScreen
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.codespacepro.wallpapercompose.screen.DetailScreen
import com.codespacepro.wallpapercompose.screen.HomeScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.Detail.route,
            arguments = listOf(
                navArgument("image") {
                    type = NavType.StringType
                },
                navArgument("photographer") {
                    type = NavType.StringType
                }
            )
        ) {
            val image = it.arguments?.getString("image")
            val photographer = it.arguments?.getString("photographer")

            Log.d("NAVIGATION", "SetupNavGraph: $image \n $photographer")
            DetailScreen(navController, image, photographer)
        }
        composable(route = Screen.About.route) {
            AboutUsScreen(navController)
        }
        composable(route = Screen.Setting.route) {
            SettingScreen(navController)
        }
        composable(route = Screen.Feedback.route) {
            FeedbackScreen(navController)
        }
        composable(
            route = Screen.FullScreen.route,
            arguments = listOf(
                navArgument("image") {
                    type = NavType.StringType
                },
                navArgument("photographer") {
                    type = NavType.StringType
                }
            )
        ) {
            val imageUrl = it.arguments?.getString("image")
            val photographer = it.arguments?.getString("photographer")
            FullScreen(navController, imageUrl = imageUrl, photographer)
        }
        composable(route = Screen.Favourite.route){
            FavouriteScreen(navController)
        }
    }
}