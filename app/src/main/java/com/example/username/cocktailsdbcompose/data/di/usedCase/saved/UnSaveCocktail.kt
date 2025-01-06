package com.example.username.cocktailsdbcompose.data.di.usedCase.saved

import com.example.username.cocktailsdbcompose.data.di.repository.RepositoryMethods
import com.example.username.cocktailsdbcompose.data.response.CocktailDTO
import javax.inject.Inject

class UnSaveCocktail @Inject constructor(
    private val repositoryMethods: RepositoryMethods
) {

    suspend operator fun invoke(cocktail: CocktailDTO, onSuccess: () -> Unit, onError: (String) -> Unit) {
        return repositoryMethods.unSavedCocktail(cocktail, onSuccess, onError)
    }

}