package com.example.username.cocktailsdbcompose.data.di.repository

import com.example.username.cocktailsdbcompose.data.di.dataStore.PreferenceMethods
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val preferences: PreferenceMethods
): RepositoryMethods {

    override suspend fun saveLanguage(key: String, value: String) {
        preferences.saveLanguage(key, value)
    }

    override suspend fun getLanguage(): Flow<String> {
        return preferences.getLanguage()
    }

}