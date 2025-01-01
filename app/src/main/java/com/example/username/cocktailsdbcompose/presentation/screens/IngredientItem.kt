package com.example.username.cocktailsdbcompose.presentation.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Text
import coil.compose.AsyncImage
import com.example.username.cocktailsdbcompose.R


@Composable
fun IngredientItem(strImage: String, strIngredient: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .border(1.dp, colorResource(R.color.orange), RoundedCornerShape(8.dp))
            .background(colorResource(R.color.graySuperDark), RoundedCornerShape(8.dp))
            .padding(12.dp)
            .size(width = 200.dp, height = 40.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            modifier = Modifier.size(40.dp),
            model = strImage,
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(start = 12.dp),
            textAlign = TextAlign.Start,
            text = strIngredient,
            maxLines = 3,
            fontSize = 10.sp,
            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
            fontWeight = FontWeight.Bold
        )
    }
}