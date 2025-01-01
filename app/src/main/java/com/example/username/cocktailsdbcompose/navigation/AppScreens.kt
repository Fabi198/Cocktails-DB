package com.example.username.cocktailsdbcompose.navigation

sealed class AppScreens (val route: String) {
    object MainScreen: AppScreens("main_screen")
    object CocktailScreen: AppScreens("cocktail_screen")
    object IngredientScreen: AppScreens("ingredient_screen")
    object SearchScreen: AppScreens("search_screen")
}