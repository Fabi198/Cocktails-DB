package com.example.username.cocktailsdbcompose.data.response

import com.example.username.cocktailsdbcompose.data.response.CocktailDTO
import com.google.gson.annotations.SerializedName

data class CocktailsDTO (@SerializedName("drinks") val cocktails: List<CocktailDTO>)