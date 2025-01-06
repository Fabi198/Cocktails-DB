package com.example.username.cocktailsdbcompose.data.di.usedCase.recents

import com.example.username.cocktailsdbcompose.data.di.repository.RepositoryMethods
import javax.inject.Inject

class GetRecentCocktails @Inject constructor(
    private val repositoryMethods: RepositoryMethods
) {

    suspend operator fun invoke() = repositoryMethods.getFavoritesCocktails()

}