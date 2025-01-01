package com.example.username.cocktailsdbcompose.data.di.usedCase.language

import com.example.username.cocktailsdbcompose.data.di.repository.RepositoryMethods
import javax.inject.Inject

class GetLanguage @Inject constructor(
    private val repositoryMethods: RepositoryMethods
) {

    suspend operator fun invoke() = repositoryMethods.getLanguage()
}