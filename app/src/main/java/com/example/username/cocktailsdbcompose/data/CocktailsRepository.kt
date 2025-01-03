package com.example.username.cocktailsdbcompose.data

import com.example.username.cocktailsdbcompose.data.response.CategoriesDTO
import com.example.username.cocktailsdbcompose.data.response.CocktailsDTO
import com.example.username.cocktailsdbcompose.data.response.CocktailsSimpleDTO
import com.example.username.cocktailsdbcompose.data.response.GlassesDTO
import com.example.username.cocktailsdbcompose.data.response.IngredientsDTO
import com.example.username.cocktailsdbcompose.data.response.KindsDTO
import retrofit2.Response
import javax.inject.Inject

class CocktailsRepository @Inject constructor(
    private val cocktailsApiService: CocktailsApiService
) {

    suspend fun getCocktailsList(id: String): Response<CocktailsDTO> {
        return cocktailsApiService.getCocktailsList(id)
    }

    suspend fun getCocktailRandom(): Response<CocktailsDTO> {
        return cocktailsApiService.getCocktailRandom()
    }

    suspend fun getCocktailsByIngredient(id: String): Response<CocktailsSimpleDTO> {
        return cocktailsApiService.getCocktailsByIngredient(id)
    }

    suspend fun getCocktailsByName(name: String): Response<CocktailsSimpleDTO> {
        return cocktailsApiService.getCocktailsByName(name)
    }

    suspend fun getCocktailsByGlass(glass: String): Response<CocktailsSimpleDTO> {
        return cocktailsApiService.getCocktailsByGlass(glass)
    }

    suspend fun getCocktailsByKind(kind: String): Response<CocktailsSimpleDTO> {
        return cocktailsApiService.getCocktailsByKind(kind)
    }

    suspend fun getCocktailsByCategory(category: String): Response<CocktailsSimpleDTO> {
        return cocktailsApiService.getCocktailsByCategory(category)
    }

    suspend fun getCocktailsByFirstLetter(letter: String): Response<CocktailsSimpleDTO> {
        return cocktailsApiService.getCocktailsByFirstLetter(letter)
    }

    suspend fun getIngredient(id: String): Response<IngredientsDTO> {
        return cocktailsApiService.getIngredient(id)
    }

    suspend fun getCategoriesList(): Response<CategoriesDTO> {
        return cocktailsApiService.getCategoriesList()
    }

    suspend fun getGlassesList(): Response<GlassesDTO> {
        return cocktailsApiService.getGlassesList()
    }

    suspend fun getKindsList(): Response<KindsDTO> {
        return cocktailsApiService.getKindsList()
    }

}