package com.example.username.cocktailsdbcompose.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.username.cocktailsdbcompose.data.CocktailsRepository
import com.example.username.cocktailsdbcompose.data.di.dataStore.Preferences
import com.example.username.cocktailsdbcompose.data.di.usedCase.language.SaveLanguage
import com.example.username.cocktailsdbcompose.data.response.CategoryDTO
import com.example.username.cocktailsdbcompose.data.response.GlassDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawerMenuViewModel @Inject constructor(
    private val repo: CocktailsRepository,
    private val saveLanguage: SaveLanguage,
    private val preferences: Preferences
): ViewModel() {
    private val _stateCategoriesDrawer = MutableStateFlow(emptyList<CategoryDTO>())
    val stateCategoriesDrawer: StateFlow<List<CategoryDTO>> get() = _stateCategoriesDrawer
    private val _stateGlassesDrawer = MutableStateFlow(emptyList<GlassDTO>())
    val stateGlassesDrawer: StateFlow<List<GlassDTO>> get() = _stateGlassesDrawer
    private val _showFastPreferences = MutableStateFlow(false)
    val showFastPreferences: StateFlow<Boolean> get() = _showFastPreferences
    private val _isLoggedIn = MutableStateFlow(true) // CAMBIAR A FALSE LO ANTES POSIBLE
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn
    private val _languageList = MutableStateFlow(listOf("Inglés", "Español", "Alemán", "Francés", "Italiano"))
    val languageList: StateFlow<List<String>> get() = _languageList
    private val _savedLanguage = MutableStateFlow("")
    val savedLanguage: StateFlow<String> get() = _savedLanguage
    private val _languageInstructions: Flow<String> = preferences.languageFlow
    val languageInstructions: Flow<String> get() = _languageInstructions


    init {
        viewModelScope.launch {
            _stateCategoriesDrawer.value = repo.getCategoriesList().body()?.drinks!!
            _stateGlassesDrawer.value = repo.getGlassesList().body()?.drinksDTO!!
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
    }
}