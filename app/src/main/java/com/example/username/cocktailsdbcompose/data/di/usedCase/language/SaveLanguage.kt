package com.example.username.cocktailsdbcompose.data.di.usedCase.language

import com.example.username.cocktailsdbcompose.data.di.repository.RepositoryMethods
import javax.inject.Inject

class SaveLanguage @Inject constructor(
    private val repositoryMethods: RepositoryMethods
) {

    suspend operator fun invoke(key: String, value: String) {
        repositoryMethods.saveLanguage(key, value)
    }
}