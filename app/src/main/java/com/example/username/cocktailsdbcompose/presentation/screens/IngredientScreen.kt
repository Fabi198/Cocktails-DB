package com.example.username.cocktailsdbcompose.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.username.cocktailsdbcompose.presentation.viewModel.IngredientScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientScreen(navController: NavController, viewModel: IngredientScreenViewModel = hiltViewModel(), idIngredient: String?) {
    LaunchedEffect(key1 = idIngredient) {
        viewModel.searchIngredient(idIngredient)
    }
    val ingredient by viewModel.stateIngredient.collectAsState()
    val usedCocktails by viewModel.stateUsedCocktails.collectAsState()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = true)
    val errorMessageIngredient: String by viewModel.errorMessageIngredient.collectAsState()
    val errorMessageCocktails: String by viewModel.errorMessageCocktails.collectAsState()
    val emptyList: Boolean by viewModel.emptyList.observeAsState(initial = false)
    val showFullDesc: Boolean by viewModel.showFullDesc.collectAsState()
    val isLoadingDescription: Boolean by viewModel.isLoadingDescription.collectAsState()
    val ingredientDescription: String by viewModel.ingredientDescription.collectAsState()

    SubBack()
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(40.dp), color = colorResource(R.color.orange))
        }
    } else {
        if (errorMessageIngredient.isNotEmpty() || emptyList) {
            Toast.makeText(LocalContext.current, errorMessageIngredient, Toast.LENGTH_SHORT).show()
            navController.navigate(route = AppScreens.MainScreen.route) {
                popUpTo(AppScreens.MainScreen.route) { inclusive = true }
            }
        } else {
            if (ingredient[0].strIngredient.toString().isNotEmpty()) {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val topGuideline = createGuidelineFromTop(130.dp)
                    val (image, title, loadingDesc, description, btnSeeMore, fullDesc, lazyGridTitle, lazyGrid) = createRefs()

                    Text(
                        modifier = Modifier
                            .padding(bottom = 40.dp)
                            .constrainAs(title) {
                                top.linkTo(topGuideline)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        color = Color.White,
                        text = ingredient[0].strIngredient.toString(),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        fontWeight = FontWeight.Bold
                    )
                    AsyncImage(
                        modifier = Modifier
                            .size(200.dp)
                            .constrainAs(image) {
                                top.linkTo(title.bottom)
                                start.linkTo(parent.start)
                            }
                            .padding(start = 20.dp),
                        model  = "https://www.thecocktaildb.com/images/ingredients/${idIngredient?.replace("_", "%20")}.png",
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    if (isLoadingDescription) {
                        Box(modifier = Modifier
                            .constrainAs(description) {
                                top.linkTo(image.top)
                                start.linkTo(image.end)
                                end.linkTo(parent.end)
                            }
                            .size(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = colorResource(R.color.orange))
                        }
                    } else {
                        Text(
                            modifier = Modifier
                                .constrainAs(description) {
                                    top.linkTo(image.top)
                                    start.linkTo(image.end)
                                    end.linkTo(parent.end)
                                }
                                .padding(end = 20.dp)
                                .fillMaxWidth(0.5f),
                            color = Color.White,
                            text = ingredientDescription,
                            fontSize = 16.sp,
                            maxLines = 5,
                            lineHeight = 1.em,
                            textAlign = TextAlign.Start,
                            fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                            fontWeight = FontWeight.Normal
                        )
                        Box(modifier = Modifier
                            .padding(top = 8.dp)
                            .constrainAs(btnSeeMore) {
                                top.linkTo(description.bottom)
                                start.linkTo(description.start)
                            }
                            .border(1.dp, colorResource(R.color.orange), RoundedCornerShape(12.dp))
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                                    .clickable {
                                        viewModel.showFullDesc(true)
                                    },
                                text = "Ver más",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.montserrat_bold, FontWeight.Bold)),
                                textAlign = TextAlign.Start,
                                color = Color.Black
                            )
                        }
                    }
                    if (showFullDesc) {
                        val scrollState = rememberScrollState()
                        BasicAlertDialog(
                            onDismissRequest = { viewModel.showFullDesc(false) },
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .fillMaxHeight(0.5f)
                                .constrainAs(fullDesc) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .background(colorResource(R.color.graySuperDark), RoundedCornerShape(12.dp))
                                .border(1.dp, colorResource(R.color.orange), RoundedCornerShape(12.dp))
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .verticalScroll(scrollState),
                                text = "$ingredientDescription\n(Traducido por Google)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(Font(R.font.montserrat_bold, FontWeight.Bold)),
                                textAlign = TextAlign.Start,
                                color = Color.White
                            )
                        }
                    }
                    Text(
                        modifier = Modifier
                            .constrainAs(lazyGridTitle) {
                                top.linkTo(image.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(8.dp)
                            .fillMaxWidth()
                            .background(colorResource(R.color.similYellow), RoundedCornerShape(8.dp))
                            .border(1.dp, colorResource(R.color.similYellow), RoundedCornerShape(8.dp)),
                        color = Color.Black,
                        text = "Se utiliza en:",
                        fontSize = 12.sp,
                        maxLines = 5,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.montserrat_semi_bold)),
                        fontWeight = FontWeight.Bold
                    )
                    if (errorMessageCocktails.isEmpty()) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier
                                .constrainAs(lazyGrid) {
                                    top.linkTo(lazyGridTitle.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .fillMaxHeight(0.5f)
                                .padding(4.dp)
                                .border(1.dp, colorResource(R.color.similYellow), RoundedCornerShape(12.dp)),
                            contentPadding = PaddingValues(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(usedCocktails) { cocktail ->
                                CocktailItem(cocktail.strDrinkThumb.toString(), cocktail.strDrink.toString()) {
                                    navController.navigate(route = AppScreens.CocktailScreen.route + "/${cocktail.idDrink}" + "/false")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}