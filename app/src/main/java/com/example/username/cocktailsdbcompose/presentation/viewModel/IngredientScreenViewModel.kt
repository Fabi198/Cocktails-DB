package com.example.username.cocktailsdbcompose.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.username.cocktailsdbcompose.data.CocktailsRepository
import com.example.username.cocktailsdbcompose.data.di.TranslatorFactory
import com.example.username.cocktailsdbcompose.data.di.dataStore.Preferences
import com.example.username.cocktailsdbcompose.data.response.CocktailSimpleDTO
import com.example.username.cocktailsdbcompose.data.response.IngredientDTO
import com.google.mlkit.nl.translate.TranslateLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientScreenViewModel @Inject constructor(private val repo: CocktailsRepository, private val translatorFactory: TranslatorFactory, private val preferences: Preferences): ViewModel() {
    private val _stateIngredient = MutableStateFlow(emptyList<IngredientDTO>())
    val stateIngredient: StateFlow<List<IngredientDTO>> get() = _stateIngredient
    private val _stateUsedCocktails = MutableStateFlow(emptyList<CocktailSimpleDTO>())
    val stateUsedCocktails: StateFlow<List<CocktailSimpleDTO>> get() = _stateUsedCocktails
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _errorMessageIngredient = MutableStateFlow("")
    val errorMessageIngredient: StateFlow<String> get() = _errorMessageIngredient
    private val _errorMessageCocktails = MutableStateFlow("")
    val errorMessageCocktails: StateFlow<String> get() = _errorMessageCocktails
    private val _emptyList = MutableLiveData<Boolean>()
    val emptyList: LiveData<Boolean> = _emptyList
    private val _showFullDesc = MutableStateFlow(false)
    val showFullDesc: StateFlow<Boolean> get() = _showFullDesc
    private val _languageInstructions: Flow<String> = preferences.languageFlow
    val languageInstructions: Flow<String> get() = _languageInstructions
    private val _ingredientDescription = MutableStateFlow("")
    val ingredientDescription: StateFlow<String> get() = _ingredientDescription
    private val _isLoadingDescription = MutableStateFlow(false)
    val isLoadingDescription: StateFlow<Boolean> get() = _isLoadingDescription


    fun searchIngredient(idIngredient: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (idIngredient != null) {
                    val ingredientResponse = repo.getIngredient(idIngredient)
                    if (ingredientResponse.isSuccessful) {
                        val ingredientBody = ingredientResponse.body()
                        if (ingredientBody != null && ingredientBody.ingredient.isNotEmpty()) {
                            _stateIngredient.value = ingredientBody.ingredient
                            _isLoadingDescription.value = true
                            translateText(_stateIngredient.value[0].strDescription ?: "")
                            try {
                                if (ingredientBody.ingredient[0].strIngredient.toString().isNotEmpty()) {
                                    val cocktailsResponse = repo.getCocktailsByIngredient(ingredientBody.ingredient[0].strIngredient.toString())
                                    if (cocktailsResponse.isSuccessful) {
                                        val cocktailsBody = cocktailsResponse.body()
                                        if (cocktailsBody != null && cocktailsBody.cocktails.isNotEmpty()) {
                                            _stateUsedCocktails.value = cocktailsBody.cocktails
                                        } else {
                                            _stateUsedCocktails.value = emptyList()
                                            _emptyList.value = true
                                        }
                                    } else {
                                        handleErrorCocktails("Error en la respuesta del servidor 2: ${cocktailsResponse.message()}")
                                        return@launch
                                    }
                                }
                            } catch (e: Exception) {
                                handleErrorCocktails("Error inesperado 2: $e")
                                return@launch
                            }
                        } else {
                            _stateIngredient.value = emptyList()
                            _emptyList.value = true
                        }
                    } else {
                        handleErrorIngredient("Error en la respuesta del servidor 1: ${ingredientResponse.message()}")
                        return@launch
                    }
                }
            } catch (e: Exception) {
                handleErrorIngredient("Error inesperado 1: $e")
                return@launch
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun handleErrorIngredient(error: String) {
        _errorMessageIngredient.value = error
    }

    private fun handleErrorCocktails(error: String) {
        _errorMessageCocktails.value = error
    }

    fun showFullDesc(b: Boolean) {
        _showFullDesc.value = b
    }

    private fun translateText(text: String) {
        viewModelScope.launch {
            val target = _languageInstructions.firstOrNull()?.let { language ->
                when (language.lowercase()) {
                    "english" -> TranslateLanguage.ENGLISH
                    "spanish" -> TranslateLanguage.SPANISH
                    "german" -> TranslateLanguage.GERMAN
                    "french" -> TranslateLanguage.FRENCH
                    "italian" -> TranslateLanguage.ITALIAN
                    else -> TranslateLanguage.ENGLISH
                }
            }
            val translator = translatorFactory.createTranslator(target ?: TranslateLanguage.ENGLISH)
            translatorFactory.ensureModelDownloaded(
                translator,
                onSuccess = {
                    translator.translate(text)
                        .addOnSuccessListener { translatedText ->
                            _ingredientDescription.value = translatedText
                            _isLoadingDescription.value = false
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Translator", "Translation failed: ${exception.message}")
                            _ingredientDescription.value = text
                            _isLoadingDescription.value = false
                        }
                        .addOnCompleteListener {
                            translator.close()
                        }
                },
                onError = {
                    Log.i("rtef", it.toString())
                    _ingredientDescription.value = text
                    _isLoadingDescription.value = false
                }
            )
        }
    }
}