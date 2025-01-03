package com.example.username.cocktailsdbcompose.data.di.dataStore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.username.cocktailsdbcompose.data.di.dataStore.PreferenceKeys.AUTHENTICATED_KEY
import com.example.username.cocktailsdbcompose.data.di.dataStore.PreferenceKeys.LANGUAGE_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PREFERENCES_NAME = "cocktailsPreferences"

private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

object PreferenceKeys {
    val LANGUAGE_KEY = stringPreferencesKey("language")
    val AUTHENTICATED_KEY = booleanPreferencesKey("authenticated")
}

class Preferences @Inject constructor(
    @ApplicationContext private val context: Context
): PreferenceMethods {

    val languageFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY] ?: "english"
    }

    val isAuthenticated: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[AUTHENTICATED_KEY] ?: false
        }

    override suspend fun saveLanguage(key: String, value: String) {
        context.dataStore.edit { preference ->
            preference[LANGUAGE_KEY] = value
        }
    }

    override suspend fun getLanguage(): Flow<String> {
        return languageFlow
    }

    override suspend fun saveAuthenticationState(isAuthenticate: Boolean) {
        context.dataStore.edit { preference ->
            preference[AUTHENTICATED_KEY] = isAuthenticate
        }
    }


}