package com.example.username.cocktailsdbcompose.data.di.dataStore

import kotlinx.coroutines.flow.Flow

interface PreferenceMethods {

    suspend fun saveLanguage(key: String, value: String)
    suspend fun getLanguage(): Flow<String>

}