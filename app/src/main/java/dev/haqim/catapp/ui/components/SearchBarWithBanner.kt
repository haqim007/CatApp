package dev.haqim.catapp.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.haqim.catapp.R
import dev.haqim.catapp.ui.components.SearchBar

@Composable
fun SearchBarWithBanner(
    modifier: Modifier = Modifier,
    searchQuery: String = "",
    onSearchValueChange: (value: String) -> Unit,
    @DrawableRes banner: Int = R.drawable.wallpaperflarecom_cat
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = banner),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(160.dp),
            contentScale = ContentScale.Crop
        )
        SearchBar(searchValue = searchQuery, onValueChange = onSearchValueChange)
    }
}