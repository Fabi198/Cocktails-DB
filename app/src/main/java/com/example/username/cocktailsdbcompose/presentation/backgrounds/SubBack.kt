package com.example.username.cocktailsdbcompose.presentation.backgrounds

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.username.cocktailsdbcompose.R


@Composable
fun SubBack() {
    Image(modifier = Modifier.fillMaxSize(), painter = painterResource(R.drawable.background_fragment), contentScale = ContentScale.FillBounds, contentDescription = "null", alpha = 0.9f)
}