package com.example.basicweatherapplication.model.weather

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TemperatureDetailsModel(
    val temperature: Double,
    val humidity: Int
) : Parcelable