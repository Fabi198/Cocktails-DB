package com.example.username.cocktailsdbcompose.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.username.cocktailsdbcompose.data.CocktailsRepository
import com.example.username.cocktailsdbcompose.data.response.CocktailSimpleDTO
import com.example.username.cocktailsdbcompose.data.response.CocktailsSimpleDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val repo: CocktailsRepository): ViewModel() {
    private val _stateCocktails = MutableStateFlow(emptyList<CocktailSimpleDTO>())
    val stateCocktails: StateFlow<List<CocktailSimpleDTO>> get() = _stateCocktails
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> get() = _errorMessage
    private val _emptyList = MutableStateFlow(false)
    val emptyList: StateFlow<Boolean> get() = _emptyList

    fun searchCocktails(toSearch: String, internalCode: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val cocktailsResponse = when (internalCode) {
                    0 -> { repo.getCocktailsByName(toSearch) }
                    1 -> { repo.getCocktailsByGlass(toSearch) }
                    2 -> { repo.getCocktailsByKind(toSearch) }
                    3 -> { repo.getCocktailsByCategory(toSearch) }
                    4 -> { repo.getCocktailsByFirstLetter(toSearch) }
                    else -> { repo.getCocktailsByName(toSearch) }
                }
                if (cocktailsResponse.isSuccessful) {
                    val body = cocktailsResponse.body()
                    if (body != null) {
                        if (body.cocktails.isNotEmpty()) {
                            _stateCocktails.value = body.cocktails
                        } else {
                            _emptyList.value = true
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
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun handlerError(error: String) {
        _errorMessage.value = error
    }
}