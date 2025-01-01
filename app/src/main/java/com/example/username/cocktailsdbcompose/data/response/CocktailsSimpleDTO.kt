package com.example.username.cocktailsdbcompose.data.response

import com.example.username.cocktailsdbcompose.data.response.CocktailSimpleDTO
import com.google.gson.annotations.SerializedName

data class CocktailsSimpleDTO(@SerializedName("drinks") val cocktails: List<CocktailSimpleDTO>)
