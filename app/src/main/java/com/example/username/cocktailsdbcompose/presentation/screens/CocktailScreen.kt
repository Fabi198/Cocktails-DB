package com.example.username.cocktailsdbcompose.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.username.cocktailsdbcompose.R
import com.example.username.cocktailsdbcompose.navigation.AppScreens
import com.example.username.cocktailsdbcompose.presentation.backgrounds.SubBack
import com.example.username.cocktailsdbcompose.presentation.viewModel.CocktailScreenViewModel


@Composable
fun CocktailScreen(navController: NavController, viewModel: CocktailScreenViewModel = hiltViewModel(), idDrink: String?, random: Boolean?) {
    LaunchedEffect(key1 = idDrink, key2 = random) {
        viewModel.searchCocktail(random, idDrink)
    }
    val cocktail by viewModel.stateCocktail.collectAsState()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = true)
    val languageInstructions by viewModel.languageInstructions.collectAsState("english")
    val errorMessage by viewModel.errorMessage.collectAsState()
    val emptyList by viewModel.emptyList.collectAsState()
    SubBack()
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(40.dp), color = colorResource(R.color.orange))
        }
    } else {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val topGuideline = createGuidelineFromTop(130.dp)
            val (image, title, btnAlcoholic, btnGlass, btnCategory, instructions, ingredientsCarousel) = createRefs()

            if (errorMessage.isNotEmpty()) {
                Toast.makeText(LocalContext.current, errorMessage, Toast.LENGTH_SHORT).show()
                navController.navigate(route = AppScreens.MainScreen.route) {
                    popUpTo(AppScreens.MainScreen.route) { inclusive = true }
                }
            } else {
                if (emptyList) {
                    Toast.makeText(LocalContext.current, "No hay resultados", Toast.LENGTH_SHORT).show()
                    navController.navigate(route = AppScreens.MainScreen.route) {
                        popUpTo(AppScreens.MainScreen.route) { inclusive = true }
                    }
                } else {
                    Text(
                        modifier = Modifier
                            .padding(bottom = 40.dp)
                            .constrainAs(title) {
                                top.linkTo(topGuideline)
                                start.linkTo(image.start)
                                end.linkTo(image.end)
                            },
                        color = Color.White,
                        text = cocktail[0].strDrink!!,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        fontWeight = FontWeight.Bold
                    )
                    AsyncImage(
                        modifier = Modifier
                            .size(180.dp)
                            .clip(CircleShape)
                            .constrainAs(image) {
                                top.linkTo(title.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .border(0.75.dp, colorResource(R.color.orange), CircleShape),
                        model  = cocktail[0].strDrinkThumb,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    BtnCocktailScreen(modifier = Modifier
                        .constrainAs(btnAlcoholic) {
                            top.linkTo(image.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(btnGlass.start)
                        }
                        .padding(top = 30.dp),
                        title = cocktail[0].strAlcoholic.toString()
                    ) {
                        navController.navigate(AppScreens.SearchScreen.route + "/${cocktail[0].strAlcoholic.toString().replace(" ", "_")}" + "/2")
                    }
                    BtnCocktailScreen(modifier = Modifier
                        .constrainAs(btnGlass) {
                            top.linkTo(image.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(top = 30.dp),
                        title = cocktail[0].strGlass.toString()
                    ) {
                        navController.navigate(AppScreens.SearchScreen.route + "/${cocktail[0].strGlass.toString().replace(" ", "_")}" + "/1")
                    }
                    BtnCocktailScreen(modifier = Modifier
                        .constrainAs(btnCategory) {
                            top.linkTo(image.bottom)
                            start.linkTo(btnGlass.end)
                            end.linkTo(parent.end)
                        }
                        .padding(top = 30.dp),
                        title = cocktail[0].strCategory.toString()
                    ) {
                        navController.navigate(AppScreens.SearchScreen.route + "/${cocktail[0].strCategory.toString().replace(" ", "_")}" + "/3")
                    }
                    Box (
                        modifier = Modifier
                            .constrainAs(instructions) {
                                top.linkTo(btnAlcoholic.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(32.dp)
                            .background(colorResource(R.color.grayDark), RoundedCornerShape(8.dp))
                            .border(1.dp, colorResource(R.color.white), RoundedCornerShape(8.dp)),
                    ) {
                        when (languageInstructions) {
                            "english" -> if(cocktail[0].strInstructionsEN != null) cocktail[0].strInstructionsEN else cocktail[0].strInstructionsEN
                            "spanish" -> if(cocktail[0].strInstructionsES != null) cocktail[0].strInstructionsES else cocktail[0].strInstructionsEN
                            "german" -> if(cocktail[0].strInstructionsDE != null) cocktail[0].strInstructionsDE else cocktail[0].strInstructionsEN
                            "french" -> if(cocktail[0].strInstructionsFR != null) cocktail[0].strInstructionsFR else cocktail[0].strInstructionsEN
                            "italian" -> if(cocktail[0].strInstructionsIT != null) cocktail[0].strInstructionsIT else cocktail[0].strInstructionsEN
                            else -> cocktail[0].strInstructionsEN
                        }?.let {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = it,
                                fontWeight = FontWeight.Normal,
                                fontSize = 10.sp,
                                lineHeight = 1.2.em,
                                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                                color = colorResource(R.color.white)
                            )
                        }
                    }
                    val ingredients = viewModel.ingredients.collectAsState()
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .constrainAs(ingredientsCarousel) {
                                top.linkTo(instructions.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(ingredients.value) { ingredient ->
                            IngredientItem(ingredient.strImageSource, "${ingredient.strMeasure} x ${ingredient.strIngredient}", modifier = Modifier.padding(end = 8.dp, top = 8.dp)) {
                                navController.navigate(route = AppScreens.IngredientScreen.route + "/${ingredient.strIngredient}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BtnCocktailScreen(modifier: Modifier = Modifier, title: String, onClick: () -> Unit) {
    Button(
        modifier = modifier.fillMaxWidth(0.3f),
        onClick = { onClick() },
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 4.dp, bottomEnd = 20.dp, bottomStart = 4.dp),
        border = BorderStroke(2.dp, colorResource(R.color.orange)),
        colors = ButtonColors(
            containerColor = colorResource(R.color.graySuperDark),
            contentColor = colorResource(R.color.orange),
            disabledContainerColor = colorResource(R.color.graySuperDark),
            disabledContentColor = colorResource(R.color.orange)
        )
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
            modifier = Modifier.padding(4.dp),
            color = colorResource(R.color.orange)
        )
    }
}

