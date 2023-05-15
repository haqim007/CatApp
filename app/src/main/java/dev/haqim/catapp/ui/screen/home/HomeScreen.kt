package dev.haqim.catapp.ui.screen.home

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import dev.haqim.catapp.ui.common.CatListContent
import dev.haqim.catapp.ui.screen.navigation.Screen

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    navController: NavHostController
) {
    val uiAction: (HomeUiAction) -> Unit =
        {action: HomeUiAction -> viewModel.processAction(action)}
    val uiState by viewModel.uiState.collectAsState()
    val openCatId by rememberUpdatedState(newValue = uiState.openCatId)
    val currentSearchQuery by rememberUpdatedState(newValue = uiState.searchQuery)
    val cats = uiState.cats

    LaunchedEffect(openCatId){
        openCatId?.let {
            navController.navigate(
                Screen.DetailCat.createRoute(it)
            )
            uiAction(HomeUiAction.OpenCat(null))
        }
    }

    LaunchedEffect(currentSearchQuery){
        uiAction(HomeUiAction.OnLoad)
    }

    CatListContent(
        modifier = modifier,
        searchQuery = currentSearchQuery,
        cats = cats,
        onSearchValueChange = {value -> uiAction(HomeUiAction.SetQuery(value))},
        onOpenCat = { id: String -> uiAction(HomeUiAction.OpenCat(id)) }
    )
}
