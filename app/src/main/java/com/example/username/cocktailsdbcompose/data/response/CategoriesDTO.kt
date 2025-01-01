package com.example.username.cocktailsdbcompose.data.response

import com.google.gson.annotations.SerializedName

data class CategoriesDTO (
    @SerializedName("drinks") val drinks: List<CategoryDTO>
        )
