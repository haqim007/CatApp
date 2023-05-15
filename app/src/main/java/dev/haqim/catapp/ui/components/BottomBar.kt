package dev.haqim.catapp.ui.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.haqim.catapp.R
import dev.haqim.catapp.ui.screen.navigation.NavItem
import dev.haqim.catapp.ui.screen.navigation.Screen
import dev.haqim.catapp.ui.theme.CatAppTheme

@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route
    val navigationItems = listOf(
        NavItem(
            title = stringResource(id = R.string.menu_home),
            icon = Icons.Default.Home,
            screen = Screen.Home
        ),
        NavItem(
            title = stringResource(id = R.string.menu_favorite),
            icon = Icons.Default.Favorite,
            screen = Screen.Favorite
        ),
        NavItem(
            title = stringResource(id = R.string.menu_profile),
            icon = Icons.Default.AccountCircle,
            screen = Screen.Profile
        )
    )

    BottomNavigation(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary
    ) {
        navigationItems.forEach { navItem ->
            BottomNavigationItem(
                selected = currentRoute == navItem.screen.route,
                onClick = {
                    navController.navigate(navItem.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                unselectedContentColor = Color.LightGray,
                label = {
                    Text(text = navItem.title)
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.title
                    )
                }
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    CatAppTheme {
        BottomBar(
            navController = rememberNavController()
        )
    }
}

