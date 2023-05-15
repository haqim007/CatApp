package dev.haqim.catapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.haqim.catapp.ui.theme.CatAppTheme

@Composable
fun Loader(modifier: Modifier = Modifier){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
@Preview(showBackground = true)
fun LoaderPreview(modifier: Modifier = Modifier){
    CatAppTheme {
        Loader()
    }
}

