package com.example.username.cocktailsdbcompose.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.username.cocktailsdbcompose.data.CocktailsRepository
import com.example.username.cocktailsdbcompose.data.response.CocktailDTO
import com.example.username.cocktailsdbcompose.data.response.CocktailSimpleDTO
import com.example.username.cocktailsdbcompose.data.response.IngredientSimpleDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val repo: CocktailsRepository): ViewModel() {
    private val _stateCocktailSearchText = MutableStateFlow("")
    val stateCocktailSearchText = _stateCocktailSearchText.asStateFlow()
    private val _stateIngredientSearchText = MutableStateFlow("")
    val stateIngredientSearchText = _stateIngredientSearchText.asStateFlow()
    private val _stateFavoritesCocktails = MutableStateFlow(emptyList<CocktailDTO>())
    val stateFavoritesCocktails: StateFlow<List<CocktailDTO>> get() = _stateFavoritesCocktails
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    fun onCocktailTextChanged(newText: String) {
        _stateCocktailSearchText.value = newText
    }

    fun onIngredientTextChanged(newText: String) {
        _stateIngredientSearchText.value = newText
    }

    init {
        val newCocktails = mutableListOf<CocktailDTO>()
        viewModelScope.launch {
            (11000..11007).forEach { cocktailID ->
                try {
                    val cocktailsResponse = repo.getCocktailsList(cocktailID.toString())
                    if (cocktailsResponse.isSuccessful) {
                        val body = cocktailsResponse.body()
                        if (body != null) {
                            if (body.cocktails.isNotEmpty()) {
                                newCocktails.add(body.cocktails[0])
                            }
                        } else {
                            handlerError("Error en la respuesta del servidor 2: ${cocktailsResponse.message()}")
                        }
                    } else {
                        handlerError("Error en la respuesta del servidor 1: ${cocktailsResponse.message()}")
                        return@launch
                    }
                } catch (e: Exception) {
                    handlerError("Error inesperado: $e")
                    return@launch
                }
            }
            _stateFavoritesCocktails.value = newCocktails
        }
    }

    private fun handlerError(error: String) {
        _errorMessage.value = error
    }

}