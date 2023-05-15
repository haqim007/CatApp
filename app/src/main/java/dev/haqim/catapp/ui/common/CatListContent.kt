package dev.haqim.catapp.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.haqim.catapp.R
import dev.haqim.catapp.data.mechanism.Resource
import dev.haqim.catapp.domain.model.Cat
import dev.haqim.catapp.domain.model.dummyCats
import dev.haqim.catapp.ui.components.CatItem
import dev.haqim.catapp.ui.components.PlainMessage
import dev.haqim.catapp.ui.theme.CatAppTheme

@Composable
fun CatListContent(
    modifier: Modifier = Modifier,
    searchQuery: String,
    cats: Resource<List<Cat>?>,
    onSearchValueChange: (value: String) -> Unit,
    onOpenCat: (String) -> Unit,
    @DrawableRes banner: Int = R.drawable.wallpaperflarecom_cat
) {
    Column(modifier = modifier) {
        SearchBarWithBanner(
            banner = banner,
            searchQuery = searchQuery,
            onSearchValueChange = onSearchValueChange,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        when (cats) {
            is Resource.Loading -> {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
                ) {
                    CircularProgressIndicator()
                }
            }
            is Resource.Success -> {
                if(!cats.data.isNullOrEmpty()){
                    LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
                        items(cats.data) {
                            CatItem(
                                modifier = Modifier.padding(top = 2.dp),
                                imageUrl = it.imageUrl,
                                name = it.name,
                                temperament = it.temperament,
                                onItemClick = {onOpenCat(it.id)}
                            )
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }

                    }
                }else{
                    PlainMessage(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        message = "Empty :("
                    )
                }
            }
            is Resource.Error -> {
                PlainMessage(cats.message!!, Modifier.padding(horizontal = 8.dp))
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun CatListContentPreview() {
    CatAppTheme {
        CatListContent(
            searchQuery = "",
            cats = Resource.Success(data = dummyCats),
            onSearchValueChange = {},
            onOpenCat = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CatListContentLoadingPreview() {
    CatAppTheme {
        CatListContent(
            searchQuery = "",
            cats = Resource.Loading(),
            onSearchValueChange = {},
            onOpenCat = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CatListContentErrorPreview() {
    CatAppTheme {
        CatListContent(
            searchQuery = "",
            cats = Resource.Error(message = "error occurred"),
            onSearchValueChange = {},
            onOpenCat = {}
        )
    }
}
