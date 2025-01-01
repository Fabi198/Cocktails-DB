package com.example.username.cocktailsdbcompose.data.di.repository

import kotlinx.coroutines.flow.Flow

interface RepositoryMethods {

    suspend fun saveLanguage(key: String, value: String)
    suspend fun getLanguage(): Flow<String>
}