package com.example.username.cocktailsdbcompose.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.username.cocktailsdbcompose.data.CocktailsRepository
import com.example.username.cocktailsdbcompose.data.di.dataStore.Preferences
import com.example.username.cocktailsdbcompose.data.di.usedCase.favorites.GetFavoritesCocktails
import com.example.username.cocktailsdbcompose.data.di.usedCase.favorites.SaveFavoritesCocktails
import com.example.username.cocktailsdbcompose.data.response.CocktailSimpleDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repo: CocktailsRepository,
    private val preferences: Preferences,
    private val saveFavoritesCocktails: SaveFavoritesCocktails,
    private val getFavoritesCocktails: GetFavoritesCocktails
): ViewModel() {
    private val _stateCocktailSearchText = MutableStateFlow("")
    val stateCocktailSearchText = _stateCocktailSearchText.asStateFlow()
    private val _stateIngredientSearchText = MutableStateFlow("")
    val stateIngredientSearchText = _stateIngredientSearchText.asStateFlow()
    private val _stateFavoritesCocktails = MutableStateFlow(emptyList<CocktailSimpleDTO>())
    val stateFavoritesCocktails: StateFlow<List<CocktailSimpleDTO>> get() = _stateFavoritesCocktails
    private val _isLoadingFavorites = MutableStateFlow(true)
    val isLoadingFavorites: StateFlow<Boolean> get() = _isLoadingFavorites
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage

    fun onCocktailTextChanged(newText: String) {
        _stateCocktailSearchText.value = newText
    }

    fun onIngredientTextChanged(newText: String) {
        _stateIngredientSearchText.value = newText
    }

    init {
        viewModelScope.launch {
            val currentList = getFavoritesCocktails().first()

            if (currentList.isEmpty()) {
                val ids = listOf("11000", "11001", "11002", "11003", "11004", "11005", "11006", "11007")
                val cocktails = mutableListOf<CocktailSimpleDTO>()

                Log.i("rtef", "AQUI")

                ids.forEach { id ->
                    val cocktailsResponse = repo.getCocktailsList(id)
                    if (cocktailsResponse.isSuccessful) {
                        val cocktailsBody = cocktailsResponse.body()
                        if (cocktailsBody != null && cocktailsBody.cocktails.isNotEmpty()) {
                            val cocktailSimple = CocktailSimpleDTO(
                                idDrink = cocktailsBody.cocktails[0].idDrink,
                                strDrink = cocktailsBody.cocktails[0].strDrink,
                                strDrinkThumb = cocktailsBody.cocktails[0].strDrinkThumb
                            )
                            Log.i("rtef", cocktailSimple.toString())
                            cocktails.add(cocktailSimple)
                        }
                    } else {
                        handlerError("Error en la respuesta del servidor 2: ${cocktailsResponse.message()}")
                        return@launch
                    }
                }
                Log.i("rtef", cocktails.size.toString())
                saveFavoritesCocktails.invoke(cocktails)
                getFavoritesCocktails.invoke().collect { listCocktails ->
                    _stateFavoritesCocktails.value = listCocktails
                    _isLoadingFavorites.value = false
                    Log.i("rtef", listCocktails.toString())
                }
            } else {
                getFavoritesCocktails.invoke().collect { listCocktails ->
                    _stateFavoritesCocktails.value = listCocktails
                    _isLoadingFavorites.value = false
                    Log.i("rtef2", listCocktails.toString())
                }
            }
        }
    }

    private fun handlerError(error: String) {
        _errorMessage.value = error
    }

}