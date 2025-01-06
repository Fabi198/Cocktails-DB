package com.example.username.cocktailsdbcompose.data.di.usedCase.saved

import com.example.username.cocktailsdbcompose.data.di.repository.RepositoryMethods
import com.example.username.cocktailsdbcompose.data.response.CocktailDTO
import javax.inject.Inject

class CreateSavedCocktails @Inject constructor(
    private val repositoryMethods: RepositoryMethods
) {

    suspend operator fun invoke(cocktails: List<CocktailDTO>) {
        return repositoryMethods.createSavedCocktails(cocktails)
    }
}