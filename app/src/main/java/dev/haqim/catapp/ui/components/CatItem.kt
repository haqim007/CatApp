package dev.haqim.catapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import dev.haqim.catapp.R
import dev.haqim.catapp.ui.theme.CatAppTheme

@Composable
fun CatItem(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    name: String,
    temperament: String,
    onItemClick: () -> Unit,
) {
    ConstraintLayout(
        modifier = modifier.clickable { onItemClick() }.fillMaxWidth(),
    ) {
        val (catImage, catName, catTemperament, divider) = createRefs()

        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .constrainAs(catImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
            error = painterResource(id = R.drawable.outline_broken_image_24)
        )

        Text(
            text = name,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(catName) {
                start.linkTo(catImage.end, margin = 8.dp)
                top.linkTo(parent.top, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
                width = Dimension.fillToConstraints
            }.fillMaxWidth()
        )

        Text(
            text = temperament,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            fontSize = 14.sp,
            modifier = Modifier.constrainAs(catTemperament) {
                start.linkTo(catImage.end, margin = 8.dp)
                top.linkTo(catName.bottom, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CatItemPreview() {
    CatAppTheme {
        CatItem(
            name = "Abyssinian",
            imageUrl = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg",
            temperament = "Active, Energetic, Independent, Intelligent, Gentle",
            onItemClick = {}
        )
    }
}