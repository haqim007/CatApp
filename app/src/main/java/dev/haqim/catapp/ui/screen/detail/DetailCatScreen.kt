package dev.haqim.catapp.ui.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import dev.haqim.catapp.R
import dev.haqim.catapp.data.mechanism.Resource
import dev.haqim.catapp.domain.model.Cat
import dev.haqim.catapp.domain.model.dummyCats
import dev.haqim.catapp.ui.components.Loader
import dev.haqim.catapp.ui.components.PlainMessage
import dev.haqim.catapp.ui.theme.CatAppTheme

@Composable
fun DetailCatScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailCatViewModel,
    id: String,
    navigateBack: () -> Unit
) {
    val uiAction = {action: DetailCatUiAction -> viewModel.processAction(action)}
    val uiState by viewModel.uiState.collectAsState()
    val cat = uiState.cat
    uiAction(DetailCatUiAction.SetCatId(id))
    val catId by rememberUpdatedState(newValue = uiState.catId)

    LaunchedEffect(key1 = catId){
        uiAction(DetailCatUiAction.OnLoad)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Box {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back to previous screen",
                modifier = Modifier.padding(16.dp).clickable { navigateBack() }
            )
        }

        when (cat) {
            is Resource.Loading -> {
                Loader()
            }
            is Resource.Error -> {
                PlainMessage(message = cat.message!!)
            }
            else -> {
                cat.data?.let {data ->
                    DetailCatContent(cat = data, onFavoriteChange = {
                        uiAction(DetailCatUiAction.SetToFavorite(!it.isFavorite))
                    })
                } ?: PlainMessage(message = "No data found")
            }
        }
    }

}

@Composable
private fun DetailCatContent(
    modifier: Modifier = Modifier,
    cat: Cat,
    onFavoriteChange: (cat: Cat) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
        ) {
            AsyncImage(
                model = cat.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .align(Alignment.Center),
                placeholder = painterResource(id = R.drawable.wallpaperflarecom_cat)
            )

            Image(
                painter = painterResource(
                    id = if (cat.isFavorite) R.drawable.filled_favorite_24
                    else R.drawable.outline_favorite_24
                ),
                contentDescription = "Like this cat",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp)
                    .size(40.dp)
                    .clickable {
                        onFavoriteChange(cat)
                    }
            )
        }
        Text(text = cat.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = cat.temperament, fontSize = 11.sp)
        Text(text = cat.description, fontSize = 18.sp, modifier = Modifier.padding(vertical = 8.dp))
        Text(
            text = "Is hairless: ${if (cat.isHairless) "Yes" else "No"}",
            fontSize = 18.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Energy level", fontSize = 18.sp)
            Spacer(modifier = Modifier.weight(1f))
            for (i in 1..5) {
                if (i <= cat.energyLevel) {
                    Image(
                        painter = painterResource(id = R.drawable.filled_star_24),
                        contentDescription = null,
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.outline_star_24),
                        contentDescription = null
                    )
                }
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun DetailCatScreenPreview() {
    CatAppTheme {
        DetailCatContent(cat = dummyCats[0], onFavoriteChange = {})
    }
}