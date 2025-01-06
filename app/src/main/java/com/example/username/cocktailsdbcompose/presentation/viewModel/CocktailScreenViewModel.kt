package com.example.username.cocktailsdbcompose.presentation.viewModel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.username.cocktailsdbcompose.data.CocktailsRepository
import com.example.username.cocktailsdbcompose.data.di.FirebaseDBSingleton
import com.example.username.cocktailsdbcompose.data.di.dataStore.Preferences
import com.example.username.cocktailsdbcompose.data.di.usedCase.favorites.GetFavoritesCocktails
import com.example.username.cocktailsdbcompose.data.di.usedCase.favorites.UpdateFavoritesCocktails
import com.example.username.cocktailsdbcompose.data.di.usedCase.recents.AddRecentCocktail
import com.example.username.cocktailsdbcompose.data.di.usedCase.saved.GetSavedCocktails
import com.example.username.cocktailsdbcompose.data.di.usedCase.saved.SaveCocktail
import com.example.username.cocktailsdbcompose.data.di.usedCase.saved.UnSaveCocktail
import com.example.username.cocktailsdbcompose.data.response.CocktailDTO
import com.example.username.cocktailsdbcompose.data.response.CocktailSimpleDTO
import com.example.username.cocktailsdbcompose.data.response.IngredientSimpleDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailScreenViewModel @Inject constructor(
    private val repo: CocktailsRepository,
    private val preferences: Preferences,
    private val updateFavoritesCocktails: UpdateFavoritesCocktails,
    private val getFavoritesCocktails: GetFavoritesCocktails,
    private val db: FirebaseDBSingleton,
    private val saveCocktail: SaveCocktail,
    private val unSaveCocktail: UnSaveCocktail,
    private val getSavedCocktails: GetSavedCocktails,
    private val addRecentCocktail: AddRecentCocktail
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
    private val _stateFavoritesCocktails = MutableStateFlow<List<CocktailSimpleDTO>>(emptyList())
    val stateFavoritesCocktails: StateFlow<List<CocktailSimpleDTO>> get() = _stateFavoritesCocktails
    private val _resultUpdateFavoriteCocktail = MutableStateFlow("")
    val resultUpdateFavoriteCocktail: StateFlow<String> get() = _resultUpdateFavoriteCocktail
    private val _resultRemoveFavoriteCocktail = MutableStateFlow("")
    val resultRemoveFavoriteCocktail: StateFlow<String> get() = _resultRemoveFavoriteCocktail
    private val _resultSaveCocktail = MutableStateFlow("")
    val resultSaveCocktail: StateFlow<String> get() = _resultSaveCocktail
    private val _resultUnSaveCocktail = MutableStateFlow("")
    val resultUnSaveCocktail: StateFlow<String> get() = _resultUnSaveCocktail
    private val _alreadyOnFav = MutableStateFlow(false)
    val alreadyOnFav: StateFlow<Boolean> get() = _alreadyOnFav
    private val _alreadySaved = MutableStateFlow(false)
    val alreadySaved: StateFlow<Boolean> get() = _alreadySaved
    private val _indexFavoriteSelected = MutableStateFlow(-1)

    init {
        viewModelScope.launch {
            _stateFavoritesCocktails.value = getFavoritesCocktails.invoke().first()
            Log.i("rtef", getSavedCocktails.invoke().first().toString())
        }

    }


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
                                viewModelScope.launch {
                                    val simpleCocktail = CocktailSimpleDTO(
                                        idDrink = _stateCocktail.value[0].idDrink,
                                        strDrink = _stateCocktail.value[0].strDrink,
                                        strDrinkThumb = _stateCocktail.value[0].strDrinkThumb
                                    )
                                    addRecentCocktail.invoke(simpleCocktail)
                                }
                                checkAlreadyOnFav()
                                checkAlreadySaved()
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

    fun onClickUpdateFavoritesCocktails(index: Int, newCocktail: CocktailDTO) {
        viewModelScope.launch {
            val newSimpleCocktail = CocktailSimpleDTO(
                idDrink = newCocktail.idDrink,
                strDrink = newCocktail.strDrink,
                strDrinkThumb = newCocktail.strDrinkThumb
            )
            updateFavoritesCocktails.invoke(index, newSimpleCocktail, onSuccess = {
                _resultUpdateFavoriteCocktail.value = "Success"
                _indexFavoriteSelected.value = index
                checkAlreadyOnFav()
            }, onError = { error ->
                _resultUpdateFavoriteCocktail.value = error
            })
        }
    }

    fun onClickRemoveFavoriteCocktail() {
        viewModelScope.launch {
            val favorites = getFavoritesCocktails.invoke().first()
            val index = findCocktailIndex(_stateCocktail.value[0].idDrink!!, favorites)

            if (index != null) {
                val cocktailResponse = repo.getCocktailsList("1100$index")
                if (cocktailResponse.isSuccessful) {
                    val cocktails = cocktailResponse.body()
                    if (cocktails != null) {
                        if (cocktails.cocktails.isNotEmpty()) {
                            val simpleCocktail = CocktailSimpleDTO(
                                idDrink = cocktails.cocktails[0].idDrink,
                                strDrink = cocktails.cocktails[0].strDrink,
                                strDrinkThumb = cocktails.cocktails[0].strDrinkThumb
                            )
                            updateFavoritesCocktails.invoke(index, simpleCocktail, onSuccess = {
                                checkAlreadyOnFav()
                            }, onError = { error ->
                                _resultUpdateFavoriteCocktail.value = error
                            })
                        } else {
                            _emptyList.value = true
                        }
                    }
                }
            }
        }
    }

    fun onClickSaveCocktail() {
        viewModelScope.launch {
            saveCocktail.invoke(_stateCocktail.value[0], onSuccess = {
                _resultSaveCocktail.value = "Success"
                checkAlreadySaved()
            }, onError = { error ->
                _resultSaveCocktail.value = error
            })
        }
    }

    fun onClickUnSaveCocktail() {
        viewModelScope.launch {
            unSaveCocktail.invoke(_stateCocktail.value[0], onSuccess = {
                _resultUnSaveCocktail.value = "Success"
                checkAlreadySaved()
            }, onError = { error ->
                _resultUnSaveCocktail.value = error
            })
        }
    }

    private fun checkAlreadyOnFav() {
        viewModelScope.launch {
            val favorites = getFavoritesCocktails.invoke().first()

            val cocktailToCheck = CocktailSimpleDTO(
                idDrink = _stateCocktail.value[0].idDrink,
                strDrink = _stateCocktail.value[0].strDrink,
                strDrinkThumb = _stateCocktail.value[0].strDrinkThumb)

            _alreadyOnFav.value = favorites.any { it.idDrink == cocktailToCheck.idDrink }
        }
    }

    private fun checkAlreadySaved() {
        viewModelScope.launch {
            val saved = getSavedCocktails.invoke().first()

            val cocktailToCheck = _stateCocktail.value[0]

            _alreadySaved.value = saved.any { it.idDrink == cocktailToCheck.idDrink }
        }
    }

    private fun findCocktailIndex(idDrink: String, favorites: List<CocktailSimpleDTO>): Int? {
        return favorites.indexOfFirst { it.idDrink == idDrink }.takeIf { it != -1 }
    }
}

