package com.example.basicweatherapplication.network.location.jsonData

import com.google.gson.annotations.SerializedName

data class LocalNames(
    @SerializedName("en")
    val en: String = "",
    @SerializedName("sr")
    val sr: String = ""
)