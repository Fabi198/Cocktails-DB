package com.example.username.cocktailsdbcompose.data.di.usedCase.saved

import com.example.username.cocktailsdbcompose.data.di.repository.RepositoryMethods
import javax.inject.Inject

class GetSavedCocktails @Inject constructor(
    private val repositoryMethods: RepositoryMethods
) {

    suspend operator fun invoke() = repositoryMethods.getSavedCocktails()

}