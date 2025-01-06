package com.example.username.cocktailsdbcompose.data.di.usedCase.recents

import com.example.username.cocktailsdbcompose.data.di.repository.RepositoryMethods
import com.example.username.cocktailsdbcompose.data.response.CocktailSimpleDTO
import javax.inject.Inject

class AddRecentCocktail @Inject constructor(
    private val repositoryMethods: RepositoryMethods
) {

    suspend operator fun invoke(cocktail: CocktailSimpleDTO) {
        return repositoryMethods.addRecentCocktail(cocktail)
    }

}