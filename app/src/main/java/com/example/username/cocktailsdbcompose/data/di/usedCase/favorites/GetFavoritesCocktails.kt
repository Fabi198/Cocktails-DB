package com.example.username.cocktailsdbcompose.data.di.usedCase.favorites

import com.example.username.cocktailsdbcompose.data.di.repository.RepositoryMethods
import javax.inject.Inject

class GetFavoritesCocktails @Inject constructor(
    private val repositoryMethods: RepositoryMethods
) {

    suspend operator fun invoke() = repositoryMethods.getFavoritesCocktails()

}