package com.example.username.cocktailsdbcompose.data.response

import com.google.gson.annotations.SerializedName

data class KindsDTO (
    @SerializedName("drinks") val drinks: List<KindDTO>
)