package com.example.username.cocktailsdbcompose.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.username.cocktailsdbcompose.R


@Composable
fun CocktailItem(cocktailImage: String, cocktailTitle: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val scroll = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .background(colorResource(R.color.graySuperDark), RoundedCornerShape(12.dp))
            .border(1.dp, colorResource(R.color.grayDark), RoundedCornerShape(12.dp))
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(50.dp)
                .clip(CircleShape),
            model  = cocktailImage,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color.White,
            text = cocktailTitle,
            fontSize = 10.sp,
            maxLines = 1,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
            fontWeight = FontWeight.Normal
        )
    }
}