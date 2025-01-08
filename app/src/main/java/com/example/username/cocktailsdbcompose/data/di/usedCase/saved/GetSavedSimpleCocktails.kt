package com.example.username.cocktailsdbcompose.data.di.usedCase.saved

import com.example.username.cocktailsdbcompose.data.di.repository.RepositoryMethods
import com.example.username.cocktailsdbcompose.data.response.CocktailSimpleDTO
import javax.inject.Inject

class GetSavedSimpleCocktails @Inject constructor(
    private val repositoryMethods: RepositoryMethods
) {

    suspend operator fun invoke(): List<CocktailSimpleDTO> {
        return repositoryMethods.getSavedSimpleCocktails()
    }

}