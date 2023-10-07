package com.codespacepro.wallpapercompose.navigation.navgraph

sealed class Screen(val route: String) {
    object Home : Screen(route = "home")
    object About : Screen(route = "about")
    object Detail : Screen(route = "detail/{image}/{photographer}") {
        fun passData(imageUrl: String, photographer: String): String {
            return "detail/$imageUrl/$photographer"
        }
    }

    object Favourite : Screen(route = "favourite")
    object Feedback : Screen(route = "feedback")
    object FullScreen : Screen(route = "fullScreen/{image}/{photographer}") {
        fun passData(imageUrl: String, photographer: String): String {
            return "fullScreen/$imageUrl/$photographer"
        }
    }

    object Setting : Screen(route = "setting")

}