package com.example.username.cocktailsdbcompose.data.response

import com.google.gson.annotations.SerializedName

data class CocktailSimpleDTO(
    @SerializedName("idDrink") val idDrink: String?,
    @SerializedName("strDrink") val strDrink: String?,
    @SerializedName("strDrinkThumb") val strDrinkThumb: String?
)
