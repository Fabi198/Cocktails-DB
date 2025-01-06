package com.example.username.cocktailsdbcompose.data.di.usedCase.favorites

import com.example.username.cocktailsdbcompose.data.di.repository.RepositoryMethods
import com.example.username.cocktailsdbcompose.data.response.CocktailSimpleDTO
import javax.inject.Inject

class SaveFavoritesCocktails @Inject constructor(
    private val repositoryMethods: RepositoryMethods
) {

    suspend operator fun invoke(cocktails: List<CocktailSimpleDTO>) {
        repositoryMethods.saveFavoritesCocktails(cocktails)
    }

}