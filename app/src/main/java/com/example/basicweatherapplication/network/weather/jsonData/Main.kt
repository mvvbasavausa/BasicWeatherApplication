package com.example.basicweatherapplication.network.weather.jsonData

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp")
    val temperature: Double = 0.0,
    @SerializedName("humidity")
    val humidity: Int = 0
)