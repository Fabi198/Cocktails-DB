package com.example.username.cocktailsdbcompose.data.di.usedCase.recents

import com.example.username.cocktailsdbcompose.data.di.repository.RepositoryMethods
import com.example.username.cocktailsdbcompose.data.response.CocktailSimpleDTO
import javax.inject.Inject

class CreateRecentCocktails @Inject constructor(
    private val repositoryMethods: RepositoryMethods
) {

    suspend operator fun invoke(cocktails: List<CocktailSimpleDTO>) {
        return repositoryMethods.createRecentCocktails(cocktails)
    }

}