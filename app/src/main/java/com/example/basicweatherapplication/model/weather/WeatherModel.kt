package com.example.basicweatherapplication.model.weather

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherModel(
    val id: Int,
    val type: String,
    val icon: String,
    val description: String
) : Parcelable