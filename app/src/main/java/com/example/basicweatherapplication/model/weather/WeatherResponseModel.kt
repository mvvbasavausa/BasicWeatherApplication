package com.example.basicweatherapplication.model.weather

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherResponseModel(
    val temperatureDetailsModel: TemperatureDetailsModel,
    val weatherModelList: List<WeatherModel>
) : Parcelable