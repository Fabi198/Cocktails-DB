package com.example.username.cocktailsdbcompose.data.di.usedCase.favorites

import com.example.username.cocktailsdbcompose.data.di.repository.RepositoryMethods
import com.example.username.cocktailsdbcompose.data.response.CocktailSimpleDTO
import javax.inject.Inject

class UpdateFavoritesCocktails @Inject constructor(
    private val repositoryMethods: RepositoryMethods
){

    suspend operator fun invoke(index: Int, newCocktail: CocktailSimpleDTO, onSuccess: () -> Unit, onError: (String) -> Unit) {
        repositoryMethods.updateFavoritesCocktails(index, newCocktail, onSuccess, onError)
    }

}