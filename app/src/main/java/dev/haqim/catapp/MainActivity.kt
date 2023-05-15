package dev.haqim.catapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.haqim.catapp.di.Injection
import dev.haqim.catapp.ui.components.BottomBar
import dev.haqim.catapp.ui.components.PlainMessage
import dev.haqim.catapp.ui.screen.detail.DetailCatScreen
import dev.haqim.catapp.ui.screen.detail.DetailCatViewModel
import dev.haqim.catapp.ui.screen.favorite.FavoriteScreen
import dev.haqim.catapp.ui.screen.favorite.FavoriteViewModel
import dev.haqim.catapp.ui.screen.home.HomeScreen
import dev.haqim.catapp.ui.screen.home.HomeViewModel
import dev.haqim.catapp.ui.screen.navigation.Screen
import dev.haqim.catapp.ui.screen.profile.ProfileScreen
import dev.haqim.catapp.ui.theme.CatAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    CatApp()
                }
            }
        }
    }
}

@Composable
fun CatApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.DetailCat.route){
                BottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(viewModel = viewModel(
                    factory = HomeViewModel
                        .provideFactory(Injection.provideRepository(LocalContext.current))
                    ),
                    navController = navController
                )
            }
            composable(Screen.Favorite.route){
                FavoriteScreen(
                    viewModel = viewModel(
                        factory = FavoriteViewModel.provideFactory(
                            Injection.provideRepository(LocalContext.current)
                        )
                    ),
                    navController =  navController
                )
            }
            composable(Screen.Profile.route){
                ProfileScreen()
            }
            composable(
                Screen.DetailCat.route,
                arguments = listOf(navArgument("id"){type = NavType.StringType})
            ){
                val id = it.arguments?.getString("id")
                if (id != null) {
                    DetailCatScreen(
                        viewModel =
                        viewModel(
                            factory = DetailCatViewModel
                                .provideFactory(Injection.provideRepository(LocalContext.current))
                        ),
                        id = id,
                        navigateBack = { navController.navigateUp() }
                    )
                }else{
                    PlainMessage(message = "ID not found")
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CatAppTheme {
        CatApp()
    }
}