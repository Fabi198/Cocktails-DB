package com.example.username.cocktailsdbcompose.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.username.cocktailsdbcompose.data.CocktailsRepository
import com.example.username.cocktailsdbcompose.data.di.dataStore.Preferences
import com.example.username.cocktailsdbcompose.data.response.CocktailDTO
import com.example.username.cocktailsdbcompose.data.response.IngredientSimpleDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailScreenViewModel @Inject constructor(
    private val repo: CocktailsRepository,
    private val preferences: Preferences
) : ViewModel() {
    private val _stateCocktail = MutableStateFlow(emptyList<CocktailDTO>())
    val stateCocktail: StateFlow<List<CocktailDTO>> get() = _stateCocktail
    private val _ingredients = MutableStateFlow<List<IngredientSimpleDTO>>(emptyList())
    val ingredients: StateFlow<List<IngredientSimpleDTO>> = _ingredients
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage
    private val _emptyList = MutableStateFlow(false)
    val emptyList: StateFlow<Boolean> get() = _emptyList
    private val _languageInstructions: Flow<String> = preferences.languageFlow
    val languageInstructions: Flow<String> get() = _languageInstructions


    fun searchCocktail(random: Boolean?, idDrink: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            if (random == true || idDrink != null) {
                try {
                    val cocktailResponse = if (random == true) repo.getCocktailRandom() else repo.getCocktailsList(idDrink ?: "")
                    if (cocktailResponse.isSuccessful) {
                        val cocktails = cocktailResponse.body()
                        if (cocktails != null) {
                            if (cocktails.cocktails.isNotEmpty()) {
                                _stateCocktail.value = cocktails.cocktails
                            } else {
                                _emptyList.value = true
                            }
                        } else {
                            handlerError("Error en la respuesta del servidor 2: ${cocktailResponse.message()}")
                            return@launch
                        }
                    } else {
                        handlerError("Error en la respuesta del servidor 1: ${cocktailResponse.message()}")
                        return@launch
                    }
                } catch (e: Exception) {
                    handlerError("Error inesperado: $e")
                    return@launch
                } finally {
                    _isLoading.value = false
                }
            }
            if (_stateCocktail.value.isNotEmpty()) {
                _isLoading.value = false
                loadIngredients()
            }
        }
    }

    private fun loadIngredients() {
        viewModelScope.launch {
            val cocktail = _stateCocktail.value.firstOrNull() ?: return@launch

            val newIngredients = mutableListOf<IngredientSimpleDTO>()
            (1..15).forEach { index ->
                val ingredientName = cocktail::class
                    .members.find { it.name == "strIngredient$index" }
                    ?.call(cocktail) as? String

                val measure = cocktail::class
                    .members.find { it.name == "strMeasure$index" }
                    ?.call(cocktail) as? String

                if (!ingredientName.isNullOrEmpty()) {
                    newIngredients.add(
                        IngredientSimpleDTO(
                            ingredientName,
                            measure ?: "",
                            "https://www.thecocktaildb.com/images/ingredients/${ingredientName}-Small.png"
                        )
                    )
                }
            }
            _ingredients.value = newIngredients
        }
    }

    private fun handlerError(error: String) {
        _errorMessage.value = error
    }
}

