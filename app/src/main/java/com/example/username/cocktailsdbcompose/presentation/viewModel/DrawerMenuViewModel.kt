package com.example.username.cocktailsdbcompose.presentation.viewModel

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.username.cocktailsdbcompose.data.CocktailsRepository
import com.example.username.cocktailsdbcompose.data.di.FirebaseAuthSingleton
import com.example.username.cocktailsdbcompose.data.di.dataStore.Preferences
import com.example.username.cocktailsdbcompose.data.di.usedCase.language.SaveLanguage
import com.example.username.cocktailsdbcompose.data.response.CategoryDTO
import com.example.username.cocktailsdbcompose.data.response.GlassDTO
import com.example.username.cocktailsdbcompose.data.response.KindDTO
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class DrawerMenuViewModel @Inject constructor(
    private val repo: CocktailsRepository,
    private val saveLanguage: SaveLanguage,
    private val preferences: Preferences,
    private val firebaseAuth: FirebaseAuthSingleton
): ViewModel() {
    private val _stateCategoriesDrawer = MutableStateFlow(emptyList<CategoryDTO>())
    val stateCategoriesDrawer: StateFlow<List<CategoryDTO>> get() = _stateCategoriesDrawer
    private val _stateKindsDrawer = MutableStateFlow(emptyList<KindDTO>())
    val stateKindsDrawer: StateFlow<List<KindDTO>> get() = _stateKindsDrawer
    private val _stateGlassesDrawer = MutableStateFlow(emptyList<GlassDTO>())
    val stateGlassesDrawer: StateFlow<List<GlassDTO>> get() = _stateGlassesDrawer
    private val _showFastPreferences = MutableStateFlow(false)
    val showFastPreferences: StateFlow<Boolean> get() = _showFastPreferences
    private val _languageList = MutableStateFlow(listOf("Inglés", "Español", "Alemán", "Francés", "Italiano"))
    val languageList: StateFlow<List<String>> get() = _languageList
    private val _savedLanguage = MutableStateFlow("")
    val savedLanguage: StateFlow<String> get() = _savedLanguage
    private val _languageInstructions: Flow<String> = preferences.languageFlow
    val languageInstructions: Flow<String> get() = _languageInstructions
    private val _authState: Flow<Boolean> = preferences.isAuthenticated
    val authState: Flow<Boolean> get() = _authState
    private val _areClosingSession = MutableStateFlow(false)
    val areClosingSession: StateFlow<Boolean> get() = _areClosingSession


    init {
        viewModelScope.launch {
            _stateCategoriesDrawer.value = repo.getCategoriesList().body()?.drinks!!
            _stateGlassesDrawer.value = repo.getGlassesList().body()?.drinksDTO!!
            _stateKindsDrawer.value = repo.getKindsList().body()?.drinks!!
            getLanguageSaved()
        }
    }

    fun changeShowFastPreferences() {
        _showFastPreferences.value = !_showFastPreferences.value
    }

    fun changeLanguage(language: String) {
        viewModelScope.launch {
            val languageSafeName = when (language) {
                "Inglés" -> { "english" }
                "Español" -> { "spanish" }
                "Alemán" -> { "german" }
                "Francés" -> { "french" }
                "Italiano" -> { "italian" }
                else -> { "english" }
            }
            saveLanguage.invoke("language", languageSafeName)
            _savedLanguage.value = language
        }
    }

    private fun getLanguageSaved() {
        viewModelScope.launch {
            preferences.languageFlow.collect { language ->
                _savedLanguage.value = when(language) {
                    "english" -> { "Inglés" }
                    "spanish" -> { "Español" }
                    "german" -> { "Alemán" }
                    "french" -> { "Francés" }
                    "italian" -> { "Italiano" }
                    else -> { "Inglés" }
                }
            }
        }
    }

    fun onSignInWithGoogle(credential: Credential) {
        viewModelScope.launch {
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                firebaseAuth.auth.signInWithCredential(firebaseCredential)
                    .addOnSuccessListener {
                        viewModelScope.launch {
                            preferences.saveAuthenticationState(true)
                        }
                    }
                    .addOnFailureListener {
                        Log.i("rtef", it.message.toString())
                    }
                    .await()
            } else {
                Log.i("rtef", "UNEXPECTED_CREDENTIAL")
            }
        }
    }

    fun signOut() {
        firebaseAuth.auth.signOut()
        viewModelScope.launch {
            preferences.saveAuthenticationState(false)
        }
    }

    fun getProfilePhoto(): String {
        lateinit var uriPhoto: String
        if (firebaseAuth.auth.currentUser != null) {
            firebaseAuth.auth.currentUser!!.photoUrl?.let { uriPhoto = it.toString() }
        }
        return uriPhoto
    }
}