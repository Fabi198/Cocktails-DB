package com.example.username.cocktailsdbcompose.data.di.repository

import com.example.username.cocktailsdbcompose.data.response.CocktailDTO
import com.example.username.cocktailsdbcompose.data.response.CocktailSimpleDTO
import kotlinx.coroutines.flow.Flow

interface RepositoryMethods {

    suspend fun saveLanguage(key: String, value: String)
    suspend fun getLanguage(): Flow<String>
    suspend fun saveAuthenticationState(isAuthenticate: Boolean)
    suspend fun saveFavoritesCocktails(cocktails: List<CocktailSimpleDTO>)
    suspend fun getFavoritesCocktails(): Flow<List<CocktailSimpleDTO>>
    suspend fun updateFavoritesCocktails(index: Int, newCocktail: CocktailSimpleDTO, onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun resetFavoritesCocktails(cocktails: List<CocktailSimpleDTO>)
    suspend fun createSavedCocktails(cocktails: List<CocktailDTO>)
    suspend fun saveCocktail(cocktail: CocktailDTO, onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun getSavedCocktails(): Flow<List<CocktailDTO>>
    suspend fun unSavedCocktail(cocktail: CocktailDTO, onSuccess: () -> Unit, onError: (String) -> Unit)
    suspend fun createRecentCocktails(cocktails: List<CocktailSimpleDTO>)
    suspend fun addRecentCocktail(cocktail: CocktailSimpleDTO)
    suspend fun getRecentCocktails(): Flow<List<CocktailSimpleDTO>>
}