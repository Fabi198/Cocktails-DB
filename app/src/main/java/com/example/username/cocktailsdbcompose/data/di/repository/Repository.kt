package com.example.username.cocktailsdbcompose.data.di.repository

import com.example.username.cocktailsdbcompose.data.di.dataStore.PreferenceMethods
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val preferencesMethods: PreferenceMethods
): RepositoryMethods {

    override suspend fun saveLanguage(key: String, value: String) {
        preferencesMethods.saveLanguage(key, value)
    }

    override suspend fun getLanguage(): Flow<String> {
        return preferencesMethods.getLanguage()
    }

    override suspend fun saveAuthenticationState(isAuthenticate: Boolean) {
        return preferencesMethods.saveAuthenticationState(isAuthenticate)
    }


}