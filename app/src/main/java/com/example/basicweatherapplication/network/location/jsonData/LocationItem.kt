package com.example.basicweatherapplication.network.location.jsonData

import com.google.gson.annotations.SerializedName

data class LocationItem(
    @SerializedName("country")
    val country: String = "",
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("lon")
    val lon: Double = 0.0,
    @SerializedName("local_names")
    val localNames: LocalNames,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("state")
    val state: String = ""
)