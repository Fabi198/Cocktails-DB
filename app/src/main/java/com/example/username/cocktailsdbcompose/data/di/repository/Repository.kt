package com.example.username.cocktailsdbcompose.data.di.repository

import com.example.username.cocktailsdbcompose.data.di.dataStore.PreferenceMethods
import com.example.username.cocktailsdbcompose.data.response.CocktailDTO
import com.example.username.cocktailsdbcompose.data.response.CocktailSimpleDTO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val preferencesMethods: PreferenceMethods
): RepositoryMethods {

    override suspend fun saveLanguage(key: String, value: String) {
        preferencesMethods.saveLanguage(key, value)
    }

    override suspend fun getLanguage(): Flow<String> {
        return preferencesMethods.getLanguage()
    }

    override suspend fun saveAuthenticationState(isAuthenticate: Boolean) {
        return preferencesMethods.saveAuthenticationState(isAuthenticate)
    }

    override suspend fun saveFavoritesCocktails(cocktails: List<CocktailSimpleDTO>) {
        return preferencesMethods.saveFavoritesCocktails(cocktails)
    }

    override suspend fun getFavoritesCocktails(): Flow<List<CocktailSimpleDTO>> {
        return preferencesMethods.getFavoritesCocktails()
    }

    override suspend fun updateFavoritesCocktails(index: Int, newCocktail: CocktailSimpleDTO, onSuccess: () -> Unit, onError: (String) -> Unit) {
        return preferencesMethods.updateFavoritesCocktails(index, newCocktail, onSuccess, onError)
    }

    override suspend fun resetFavoritesCocktails(cocktails: List<CocktailSimpleDTO>) {
        return preferencesMethods.resetFavoritesCocktails(cocktails)
    }

    override suspend fun createSavedCocktails(cocktails: List<CocktailDTO>) {
        return preferencesMethods.createSavedCocktails(cocktails)
    }

    override suspend fun saveCocktail(cocktail: CocktailDTO, onSuccess: () -> Unit, onError: (String) -> Unit) {
        return preferencesMethods.saveCocktail(cocktail, onSuccess, onError)
    }



    override suspend fun getSavedCocktails(): Flow<List<CocktailDTO>> {
        return preferencesMethods.getSavedCocktails()
    }

    override suspend fun unSavedCocktail(cocktail: CocktailDTO, onSuccess: () -> Unit, onError: (String) -> Unit) {
        return preferencesMethods.unSavedCocktail(cocktail, onSuccess, onError)
    }

    override suspend fun createRecentCocktails(cocktails: List<CocktailSimpleDTO>) {
        return preferencesMethods.createRecentCocktails(cocktails)
    }

    override suspend fun addRecentCocktail(cocktail: CocktailSimpleDTO) {
        return preferencesMethods.addRecentCocktail(cocktail)
    }

    override suspend fun getRecentCocktails(): Flow<List<CocktailSimpleDTO>> {
        return preferencesMethods.getRecentCocktails()
    }


}