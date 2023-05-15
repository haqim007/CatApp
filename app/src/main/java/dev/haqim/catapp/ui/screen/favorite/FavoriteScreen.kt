package dev.haqim.catapp.ui.screen.favorite

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import dev.haqim.catapp.R
import dev.haqim.catapp.ui.common.CatListContent
import dev.haqim.catapp.ui.screen.navigation.Screen

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel,
    navController: NavHostController
) {
    val uiAction: (FavoriteUiAction) -> Unit =
        {action: FavoriteUiAction -> viewModel.processAction(action)}
    val uiState by viewModel.uiState.collectAsState()
    val openCatId by rememberUpdatedState(newValue = uiState.openCatId)
    val currentSearchQuery by rememberUpdatedState(newValue = uiState.searchQuery)
    val cats = uiState.cats

    LaunchedEffect(openCatId){
        openCatId?.let {
            navController.navigate(
                Screen.DetailCat.createRoute(it)
            )
            uiAction(FavoriteUiAction.OpenCat(null))
        }
    }

    LaunchedEffect(currentSearchQuery){
        uiAction(FavoriteUiAction.OnLoad)
    }

    CatListContent(
        modifier = modifier,
        searchQuery = currentSearchQuery,
        cats = cats,
        onSearchValueChange = {value -> uiAction(FavoriteUiAction.SetQuery(value))},
        onOpenCat = { id: String -> uiAction(FavoriteUiAction.OpenCat(id)) },
        banner = R.drawable.cat_fav_bg
    )
}
