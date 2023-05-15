package dev.haqim.catapp.ui.screen.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorite : Screen("favorite")
    object Profile : Screen("profile")
    object DetailCat: Screen("detailCat/{id}"){
        fun createRoute(id: String) = "detailCat/$id"
    }
}