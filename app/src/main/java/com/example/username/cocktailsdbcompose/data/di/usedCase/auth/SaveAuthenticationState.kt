package com.example.username.cocktailsdbcompose.data.di.usedCase.auth

import com.example.username.cocktailsdbcompose.data.di.repository.RepositoryMethods
import javax.inject.Inject

class SaveAuthenticationState @Inject constructor(
    private val repositoryMethods: RepositoryMethods
) {

    suspend operator fun invoke(isAuthenticate: Boolean) {
        repositoryMethods.saveAuthenticationState(isAuthenticate)
    }

}