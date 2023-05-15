package dev.haqim.catapp.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.haqim.catapp.R
import dev.haqim.catapp.ui.theme.CatAppTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(100.dp))
        )

        Text(text = "Muhamad Nurul Khaqim",
            modifier = Modifier.padding(vertical = 16.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = "khaqim414@gmail.com", fontSize = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    CatAppTheme {
        ProfileScreen()
    }
}