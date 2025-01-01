package com.example.username.cocktailsdbcompose.data

import com.example.username.cocktailsdbcompose.data.response.CategoriesDTO
import com.example.username.cocktailsdbcompose.data.response.CocktailsDTO
import com.example.username.cocktailsdbcompose.data.response.CocktailsSimpleDTO
import com.example.username.cocktailsdbcompose.data.response.GlassesDTO
import com.example.username.cocktailsdbcompose.data.response.IngredientsDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailsApiService {

    @GET("lookup.php")
    suspend fun getCocktailsList(@Query("i") id: String): Response<CocktailsDTO>

    @GET("random.php")
    suspend fun getCocktailRandom(): Response<CocktailsDTO>

    @GET("filter.php")
    suspend fun getCocktailsByIngredient(@Query("i") id: String): Response<CocktailsSimpleDTO>

    @GET("search.php")
    suspend fun getCocktailsByName(@Query("s") name: String): Response<CocktailsSimpleDTO>

    @GET("filter.php")
    suspend fun getCocktailsByGlass(@Query("g") name: String): Response<CocktailsSimpleDTO>

    @GET("filter.php")
    suspend fun getCocktailsByKind(@Query("a") name: String): Response<CocktailsSimpleDTO>

    @GET("filter.php")
    suspend fun getCocktailsByCategory(@Query("c") name: String): Response<CocktailsSimpleDTO>

    @GET("search.php")
    suspend fun getCocktailsByFirstLetter(@Query("f") name: String): Response<CocktailsSimpleDTO>

    @GET("search.php")
    suspend fun getIngredient(@Query("i") id: String): Response<IngredientsDTO>

    @GET("list.php?c=list")
    suspend fun getCategoriesList(): Response<CategoriesDTO>

    @GET("list.php?g=list")
    suspend fun getGlassesList(): Response<GlassesDTO>

}