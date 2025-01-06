package com.example.username.cocktailsdbcompose.data.di.dataStore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.username.cocktailsdbcompose.data.di.DBNames.FAVORITES_COCKTAILS
import com.example.username.cocktailsdbcompose.data.di.DBNames.RECENT_COCKTAILS
import com.example.username.cocktailsdbcompose.data.di.DBNames.SAVED_COCKTAILS
import com.example.username.cocktailsdbcompose.data.di.DBNames.USERS
import com.example.username.cocktailsdbcompose.data.di.FirebaseAuthSingleton
import com.example.username.cocktailsdbcompose.data.di.FirebaseDBSingleton
import com.example.username.cocktailsdbcompose.data.di.dataStore.PreferenceKeys.AUTHENTICATED_KEY
import com.example.username.cocktailsdbcompose.data.di.dataStore.PreferenceKeys.FAVORITES_KEY
import com.example.username.cocktailsdbcompose.data.di.dataStore.PreferenceKeys.LANGUAGE_KEY
import com.example.username.cocktailsdbcompose.data.di.dataStore.PreferenceKeys.RECENT_KEY
import com.example.username.cocktailsdbcompose.data.di.dataStore.PreferenceKeys.SAVED_KEY
import com.example.username.cocktailsdbcompose.data.response.CocktailDTO
import com.example.username.cocktailsdbcompose.data.response.CocktailSimpleDTO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import javax.inject.Inject

private const val PREFERENCES_NAME = "cocktailsPreferences"

private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

object PreferenceKeys {
    val LANGUAGE_KEY = stringPreferencesKey("language")
    val AUTHENTICATED_KEY = booleanPreferencesKey("authenticated")
    val FAVORITES_KEY = stringPreferencesKey("favorites")
    val SAVED_KEY = stringPreferencesKey("saved")
    val RECENT_KEY = stringPreferencesKey("recent")
}

class Preferences @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuthSingleton,
    private val firebaseDB: FirebaseDBSingleton
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

    override suspend fun saveFavoritesCocktails(cocktails: List<CocktailSimpleDTO>) {
        val json = Gson().toJson(cocktails)
        context.dataStore.edit { preferences ->
            preferences[FAVORITES_KEY] = json
        }
    }

    override suspend fun getFavoritesCocktails(): Flow<List<CocktailSimpleDTO>> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[FAVORITES_KEY]
            if (json.isNullOrEmpty()) {
                emptyList()
            } else {
                val type = object : TypeToken<List<CocktailSimpleDTO>>() {}.type
                Gson().fromJson(json, type)
            }
        }
    }

    override suspend fun updateFavoritesCocktails(index: Int, newCocktail: CocktailSimpleDTO, onSuccess: () -> Unit, onError: (String) -> Unit) {
        Log.i("rteFirebase", "index: $index, newCocktail: $newCocktail")
        require(index in 0..7) { "El índice debe estar entre 0 y 7" }
        try {
            context.dataStore.edit { preferences ->
                val json = preferences[FAVORITES_KEY]
                val currentList: MutableList<CocktailSimpleDTO> = if (!json.isNullOrEmpty()) {
                    val type = object : TypeToken<List<CocktailSimpleDTO>>() {}.type
                    Gson().fromJson(json, type)
                } else {
                    MutableList(8) { CocktailSimpleDTO(null, null, null) }
                }

                currentList[index] = newCocktail
                preferences[FAVORITES_KEY] = Gson().toJson(currentList)
            }
        } catch (e: Exception) {
            onError("Error actualizando DataStore: ${e.message}")
            return
        }
        val email = firebaseAuth.auth.currentUser?.email
        if (!email.isNullOrEmpty()) {
            Log.i("rteFirebase", email)
            val userDocRef = firebaseDB.db.collection(USERS).document(email)

            userDocRef.update("$FAVORITES_COCKTAILS.$index", "${newCocktail.idDrink}")
                .addOnSuccessListener {
                    Log.i("rteFirebase", "Updated cocktail at index: $index successfully")
                    onSuccess()
                }
                .addOnFailureListener { error ->
                    onError("Error updating cocktail: ${error.message}")
                    Log.e("rteFirebase", "Error updating cocktail: ${error.message}")
                }
        }
    }

    override suspend fun resetFavoritesCocktails(cocktails: List<CocktailSimpleDTO>) {
        val json = Gson().toJson(cocktails)
        context.dataStore.edit { preferences ->
            preferences[FAVORITES_KEY] = json
        }
        val defaultFavorites = (0..7).associate { index ->
            index.toString() to "1100${index}"
        }
        val email = firebaseAuth.auth.currentUser?.email!!
        if (email.isNotEmpty()) {
            val userDocRef = firebaseDB.db.collection(USERS).document(email)

            userDocRef.update(FAVORITES_COCKTAILS, emptyList<String>())
                .addOnSuccessListener {
                    Log.i("rteFirebase", "Success to erase old ids")
                    userDocRef.update(FAVORITES_COCKTAILS, defaultFavorites)
                        .addOnSuccessListener {
                            Log.i("rteFirebase", "Success defaults ids uploaded")
                        }
                        .addOnFailureListener { error ->
                            Log.i("rteFirebase", "Failed to upload defaults ids: ${error.message}")
                        }
                }
                .addOnFailureListener { error ->
                    Log.i("rteFirebase", "Failed to update with empty list: ${error.message}")
                }

        }
    }

    override suspend fun createSavedCocktails(cocktails: List<CocktailDTO>) {
        val json = Gson().toJson(cocktails)
        context.dataStore.edit { preferences ->
            preferences[SAVED_KEY] = json
        }
    }

    override suspend fun saveCocktail(cocktail: CocktailDTO, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val currentList = getSavedCocktails().first()

        val updatedList = if (currentList.size >= 20) {
            currentList.drop(1) + cocktail
        } else {
            currentList + cocktail
        }

        createSavedCocktails(updatedList)
        val email = firebaseAuth.auth.currentUser?.email
        if (email != null) {
            val userDocRef = firebaseDB.db.collection(USERS).document(email)

            userDocRef.get()
                .addOnSuccessListener { document ->
                    val currentListFirebase = document.get(SAVED_COCKTAILS) as? List<String> ?: emptyList()

                    val updatedListFirebase = if (currentListFirebase.size >= 20) {
                        currentListFirebase.drop(1) + cocktail.idDrink  // Elimina el más viejo
                    } else {
                        currentListFirebase + cocktail.idDrink
                    }

                    userDocRef.update(SAVED_COCKTAILS, updatedListFirebase)
                        .addOnSuccessListener {
                            onSuccess()
                            Log.i("rteFirebase", "Cocktail saved successfully")
                        }
                        .addOnFailureListener { error ->
                            onError("Failed to save cocktail: ${error.message}")
                            Log.e("rteFirebase", "Failed to save cocktail: ${error.message}")
                        }

                }
                .addOnFailureListener { error ->
                    onError("Failed to get user data: ${error.message}")
                    Log.e("rteFirebase", "Failed to get user data: ${error.message}")
                }
        }
    }



    override suspend fun getSavedCocktails(): Flow<List<CocktailDTO>> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[SAVED_KEY]
            if (json.isNullOrEmpty()) {
                emptyList()
            } else {
                val type = object : TypeToken<List<CocktailDTO>>() {}.type
                Gson().fromJson(json, type)
            }
        }
    }

    override suspend fun unSavedCocktail(cocktail: CocktailDTO, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val currentList = getSavedCocktails().first()
        val updatedList = currentList.filterNot { it.idDrink == cocktail.idDrink }
        createSavedCocktails(updatedList)

        val email = firebaseAuth.auth.currentUser?.email
        if (email != null) {
            val userDocRef = firebaseDB.db.collection(USERS).document(email)

            userDocRef.get()
                .addOnSuccessListener { document ->
                    val currentListFirebase = document.get(SAVED_COCKTAILS) as? List<String> ?: emptyList()

                    val updatedListFirebase = currentListFirebase.filterNot { it == cocktail.idDrink }

                    userDocRef.update(SAVED_COCKTAILS, updatedListFirebase)
                        .addOnSuccessListener {
                            onSuccess()
                            Log.i("rteFirebase", "Cocktail removed successfully")
                        }
                        .addOnFailureListener { error ->
                            onError("Failed to remove cocktail: ${error.message}")
                            Log.e("rteFirebase", "Failed to remove cocktail: ${error.message}")
                        }
                }
                .addOnFailureListener { error ->
                    onError("Failed to get user data: ${error.message}")
                    Log.e("rteFirebase", "Failed to get user data: ${error.message}")
                }
        }
    }

    override suspend fun createRecentCocktails(cocktails: List<CocktailSimpleDTO>) {
        val json = Gson().toJson(cocktails)
        context.dataStore.edit { preferences ->
            preferences[RECENT_KEY] = json
        }
    }

    override suspend fun addRecentCocktail(cocktail: CocktailSimpleDTO) {
        val currentList = getRecentCocktails().first()

        val updatedList = if (currentList.size >= 20) {
            currentList.drop(1) + cocktail
        } else {
            currentList + cocktail
        }

        createRecentCocktails(updatedList)
        val email = firebaseAuth.auth.currentUser?.email
        if (email != null) {
            val userDocRef = firebaseDB.db.collection(USERS).document(email)

            userDocRef.get()
                .addOnSuccessListener { document ->
                    val currentListFirebase = document.get(RECENT_COCKTAILS) as? List<String> ?: emptyList()

                    val updatedListFirebase = if (currentListFirebase.size >= 20) {
                        currentListFirebase.drop(1) + cocktail.idDrink  // Elimina el más viejo
                    } else {
                        currentListFirebase + cocktail.idDrink
                    }

                    userDocRef.update(RECENT_COCKTAILS, updatedListFirebase)
                        .addOnSuccessListener {
                            Log.i("rteFirebase", "Cocktail saved successfully")
                        }
                        .addOnFailureListener { error ->
                            Log.e("rteFirebase", "Failed to save cocktail: ${error.message}")
                        }

                }
                .addOnFailureListener { error ->
                    Log.e("rteFirebase", "Failed to get user data: ${error.message}")
                }
        }
    }

    override suspend fun getRecentCocktails(): Flow<List<CocktailSimpleDTO>> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[RECENT_KEY]
            if (json.isNullOrEmpty()) {
                emptyList()
            } else {
                val type = object : TypeToken<List<CocktailSimpleDTO>>() {}.type
                Gson().fromJson(json, type)
            }
        }
    }


}