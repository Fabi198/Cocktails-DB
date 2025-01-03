package com.example.username.cocktailsdbcompose.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material3.IconButton
import androidx.wear.compose.material3.Text
import com.example.username.cocktailsdbcompose.R
import com.example.username.cocktailsdbcompose.navigation.AppScreens
import com.example.username.cocktailsdbcompose.presentation.backgrounds.MainBack
import com.example.username.cocktailsdbcompose.presentation.viewModel.MainScreenViewModel

@Composable
fun MainScreen(navController: NavController, viewModel: MainScreenViewModel = hiltViewModel()) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val topGuideline = createGuidelineFromTop(140.dp)
        val (searchCocktail, supportingCocktailText, searchIngredient, supportingIngredientText, loadingFavoritesCocktails, titleFavCocktails, lazyCocktailsGrid, fab) = createRefs()
        val searchCocktailText by viewModel.stateCocktailSearchText.collectAsState()
        val searchIngredientText by viewModel.stateIngredientSearchText.collectAsState()
        val keyboardController = LocalSoftwareKeyboardController.current
        var textErrorCocktail by rememberSaveable { mutableStateOf(false) }
        var textErrorIngredient by rememberSaveable { mutableStateOf(false) }
        val favoritesCocktails by viewModel.stateFavoritesCocktails.collectAsState()
        fun validateCocktailText(text: String) { textErrorCocktail = text.contains("/") }
        fun validateIngredientText(text: String) { textErrorIngredient = text.contains("/") }

        MainBack()
        TextField(
            modifier = Modifier
                .constrainAs(searchCocktail) {
                    top.linkTo(topGuideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .border(0.1.dp, Color.LightGray, if (!textErrorCocktail) RoundedCornerShape(12.dp) else { RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp) }),
            value = searchCocktailText,
            onValueChange = { newText -> viewModel.onCocktailTextChanged(newText); validateCocktailText(newText) },
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily(Font(R.font.montserrat_light)),
                fontWeight = FontWeight.Normal
            ),
            placeholder = {
                Text(
                    color = Color.White,
                    text = "Buscar un cocktail",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start,
                    fontFamily = FontFamily(Font(R.font.montserrat_light)),
                    fontWeight = FontWeight.Normal
                )
            },
            isError = textErrorCocktail,
            shape = if (!textErrorCocktail) RoundedCornerShape(12.dp) else { RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp) },
            trailingIcon = {
                IconButton(onClick = {
                    if (searchCocktailText.isNotEmpty()) {
                        navController.navigate(AppScreens.SearchScreen.route + "/${searchCocktailText.replace(" ", "")}" + "/0")
                        viewModel.onCocktailTextChanged("")
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboardController?.hide()
                if (searchCocktailText.isNotEmpty()) {
                    navController.navigate(AppScreens.SearchScreen.route + "/${searchCocktailText.replace(" ", "")}" + "/0")
                    viewModel.onCocktailTextChanged("")
                }
            }
            ),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        if (textErrorCocktail) {
            Text(
                modifier = Modifier.constrainAs(supportingCocktailText) {
                    start.linkTo(searchCocktail.start)
                    top.linkTo(searchCocktail.bottom)
                }
                    .padding(top = 4.dp, start = 4.dp),
                color = Color.White,
                text = "El texto no puede contener: /",
                fontSize = 10.sp,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily(Font(R.font.montserrat_italic)),
                fontWeight = FontWeight.Normal
            )
        }
        TextField(
            modifier = Modifier
                .constrainAs(searchIngredient) {
                    top.linkTo(searchCocktail.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 24.dp)
                .border(0.1.dp, Color.LightGray, if (!textErrorIngredient) RoundedCornerShape(12.dp) else { RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp) }),
            value = searchIngredientText,
            onValueChange = { newText -> viewModel.onIngredientTextChanged(newText); validateIngredientText(newText) },
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily(Font(R.font.montserrat_light)),
                fontWeight = FontWeight.Normal
            ),
            placeholder = {
                Text(
                    color = Color.White,
                    text = "Buscar un ingrediente",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start,
                    fontFamily = FontFamily(Font(R.font.montserrat_light)),
                    fontWeight = FontWeight.Normal
                )
            },
            isError = textErrorIngredient,
            shape = if (!textErrorIngredient) RoundedCornerShape(12.dp) else { RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp) },
            trailingIcon = {
                IconButton(onClick = {
                    if (searchIngredientText.isNotEmpty()) {
                        navController.navigate(route = AppScreens.IngredientScreen.route + "/$searchIngredientText")
                        viewModel.onIngredientTextChanged("")
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboardController?.hide()
                if (searchIngredientText.isNotEmpty()) {
                    navController.navigate(route = AppScreens.IngredientScreen.route + "/$searchIngredientText")
                    viewModel.onIngredientTextChanged("")
                }
            }
            ),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        if (textErrorIngredient) {
            Text(
                modifier = Modifier.constrainAs(supportingIngredientText) {
                    start.linkTo(searchIngredient.start)
                    top.linkTo(searchIngredient.bottom)
                }
                    .padding(top = 4.dp, start = 4.dp),
                color = Color.White,
                text = "El texto no puede contener: /",
                fontSize = 10.sp,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily(Font(R.font.montserrat_italic)),
                fontWeight = FontWeight.Normal
            )
        }
        if (favoritesCocktails.isNotEmpty()) {
            Text(
                modifier = Modifier.constrainAs(titleFavCocktails) {
                    top.linkTo(searchIngredient.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                    .padding(top = 40.dp),
                color = Color.White,
                text = "Cocktails Favoritos",
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontWeight = FontWeight.Bold
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .constrainAs(lazyCocktailsGrid) {
                        top.linkTo(titleFavCocktails.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favoritesCocktails) { cocktail ->
                    CocktailItem(cocktail.strDrinkThumb.toString(), cocktail.strDrink.toString()) {
                        navController.navigate(route = AppScreens.CocktailScreen.route + "/${cocktail.idDrink}" + "/false")
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(loadingFavoritesCocktails) {
                        top.linkTo(searchIngredient.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(100.dp)
                , contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = colorResource(R.color.orange))
            }
        }
        FloatingActionButton(onClick = {
            navController.navigate(route = AppScreens.CocktailScreen.route + "/0" + "/true")
        }, containerColor = Color.Transparent, modifier = Modifier
            .constrainAs(fab) { bottom.linkTo(parent.bottom); end.linkTo(parent.end) }
            .padding(end = 30.dp, bottom = 70.dp)) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .border(1.dp, Color.Black, shape = CircleShape)
                    .background(shape = CircleShape, color = colorResource(R.color.orange))
            ) {
                Image(
                    painter = painterResource(R.drawable.two_dice),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}