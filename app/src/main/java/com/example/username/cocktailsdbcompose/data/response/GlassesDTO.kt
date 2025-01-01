package com.example.username.cocktailsdbcompose.data.response

import com.example.username.cocktailsdbcompose.data.response.GlassDTO
import com.google.gson.annotations.SerializedName

data class GlassesDTO (
    @SerializedName("drinks") val drinksDTO: List<GlassDTO>
        )
