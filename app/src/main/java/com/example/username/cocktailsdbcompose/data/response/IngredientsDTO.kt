package com.example.username.cocktailsdbcompose.data.response

import com.example.username.cocktailsdbcompose.data.response.IngredientDTO
import com.google.gson.annotations.SerializedName

data class IngredientsDTO (
    @SerializedName("ingredients") val ingredient: List<IngredientDTO>
        )