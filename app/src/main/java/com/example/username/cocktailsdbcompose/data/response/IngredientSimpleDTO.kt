package com.example.username.cocktailsdbcompose.data.response

import com.google.gson.annotations.SerializedName

data class IngredientSimpleDTO (
    @SerializedName("strIngredient") val strIngredient: String?,
    @SerializedName("strMeasure") val strMeasure: String?,
    @SerializedName("strImageSource") val strImageSource: String
        )