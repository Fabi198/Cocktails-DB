package com.example.username.cocktailsdbcompose.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.username.cocktailsdbcompose.presentation.screens.CocktailScreen
import com.example.username.cocktailsdbcompose.presentation.screens.IngredientScreen
import com.example.username.cocktailsdbcompose.presentation.screens.MainScreen
import com.example.username.cocktailsdbcompose.presentation.screens.SearchScreen

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(navController = navController, startDestination = AppScreens.MainScreen.route) {
        composable(route = AppScreens.MainScreen.route) {
            MainScreen(navController, hiltViewModel())
        }
        composable(route = AppScreens.CocktailScreen.route + "/{idDrink}" + "/{random}", arguments = listOf(navArgument(name = "idDrink") { type = NavType.StringType }, navArgument(name = "random") { type = NavType.BoolType })) {
            CocktailScreen(navController, hiltViewModel(), it.arguments?.getString("idDrink"), it.arguments?.getBoolean("random"))
        }
        composable(route = AppScreens.IngredientScreen.route + "/{idIngredient}", arguments = listOf(navArgument(name = "idIngredient") { type = NavType.StringType })) {
            IngredientScreen(navController, hiltViewModel(), it.arguments?.getString("idIngredient")?.replace(" ", "_"))
        }
        composable(route = AppScreens.SearchScreen.route + "/{toSearch}" + "/{internalCode}", arguments = listOf(navArgument(name = "toSearch") { type = NavType.StringType }, navArgument(name = "internalCode") { type = NavType.IntType })) {
            SearchScreen(navController, hiltViewModel(), it.arguments?.getString("toSearch"), it.arguments?.getInt("internalCode"))
        }
    }
}