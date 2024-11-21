package com.example.basicweatherapplication.network.weather.jsonData

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("main")
    val main: String = "",
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("description")
    val description: String = ""
)