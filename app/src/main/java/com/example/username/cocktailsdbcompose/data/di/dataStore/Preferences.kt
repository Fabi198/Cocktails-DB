package com.example.username.cocktailsdbcompose.data.di.dataStore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PREFERENCES_NAME = "cocktailsPreferences"

private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

object LanguagePreferenceKeys {
    val LANGUAGE_KEY = stringPreferencesKey("language")
}

class Preferences @Inject constructor(
    @ApplicationContext private val context: Context
): PreferenceMethods {

    val languageFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LanguagePreferenceKeys.LANGUAGE_KEY] ?: "english"
    }

    override suspend fun saveLanguage(key: String, value: String) {
        context.dataStore.edit { preference ->
            preference[LanguagePreferenceKeys.LANGUAGE_KEY] = value
        }
    }

    override suspend fun getLanguage(): Flow<String> {
        return languageFlow
    }


}