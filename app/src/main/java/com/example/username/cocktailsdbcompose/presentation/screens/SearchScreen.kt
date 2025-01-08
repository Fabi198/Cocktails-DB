package com.example.username.cocktailsdbcompose.presentation.screens

import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.username.cocktailsdbcompose.R
import com.example.username.cocktailsdbcompose.navigation.AppScreens
import com.example.username.cocktailsdbcompose.presentation.backgrounds.SubBack
import com.example.username.cocktailsdbcompose.presentation.viewModel.SearchScreenViewModel

@Composable
fun SearchScreen(navController: NavController, viewModel: SearchScreenViewModel = hiltViewModel(), toSearch: String?, internalCode: Int?) {
    LaunchedEffect(key1 = toSearch, key2 = internalCode) {
        if (toSearch != null && internalCode != null) {
            val safeString = toSearch.replace("%20#%20", "%20/%20")
            viewModel.searchCocktails(safeString, internalCode)
        }

    }
    val cocktails by viewModel.stateCocktails.collectAsState()
    val isLoading: Boolean by viewModel.isLoading.observeAsState(initial = true)
    val emptyList by viewModel.emptyList.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val savedCounter by viewModel.savedCounter.collectAsState()

    SubBack()
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier.size(40.dp), color = colorResource(R.color.orange))
        }
    } else {
        ConstraintLayout() {
            val (title, savedCounterText, lazyGrid) = createRefs()

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .padding(start = 50.dp, top = 50.dp)
                    .fillMaxWidth(0.4f),
                color = colorResource(R.color.orange),
                text = toSearch.toString().replace("_", " "),
                fontSize = 16.sp,
                maxLines = 1,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily(Font(R.font.montserrat_italic)),
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier
                    .constrainAs(savedCounterText) {
                        top.linkTo(title.bottom)
                        end.linkTo(parent.end)
                    }
                    .padding(end = 14.dp, top = 12.dp),
                color = colorResource(R.color.white),
                text = savedCounter,
                fontSize = 18.sp,
                maxLines = 1,
                textAlign = TextAlign.Start,
                fontFamily = FontFamily(Font(R.font.montserrat_bold_italic)),
                fontWeight = FontWeight.Bold
            )
            if (emptyList || errorMessage.isNotEmpty()) {
                Toast.makeText(LocalContext.current, if (emptyList) "No hay resultados" else errorMessage, Toast.LENGTH_SHORT).show()
                navController.navigate(route = AppScreens.MainScreen.route) {
                    popUpTo(AppScreens.MainScreen.route) { inclusive = true }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .constrainAs(lazyGrid) {
                            top.linkTo(savedCounterText.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(top = 4.dp)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cocktails) { cocktail ->
                        CocktailItem(cocktail.strDrinkThumb.toString(), cocktail.strDrink.toString()) {
                            navController.navigate(route = AppScreens.CocktailScreen.route + "/${cocktail.idDrink}" + "/false")
                        }
                    }
                }
            }
        }
    }
}