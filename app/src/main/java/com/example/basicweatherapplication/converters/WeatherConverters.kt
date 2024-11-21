package com.example.basicweatherapplication.converters

import com.example.basicweatherapplication.model.weather.TemperatureDetailsModel
import com.example.basicweatherapplication.model.weather.WeatherModel
import com.example.basicweatherapplication.model.weather.WeatherResponseModel
import com.example.basicweatherapplication.network.weather.jsonData.WeatherResponse

fun WeatherResponse.convertToWeatherResponseModel() : WeatherResponseModel {
    val temperatureDetailsModel = TemperatureDetailsModel(temperature = this.main.temperature, humidity = this.main.humidity)
    val weatherModelList = mutableListOf<WeatherModel>()
    this.weather.forEach { weather ->
        val weatherModel = WeatherModel(id = weather.id, type = weather.main, icon = weather.icon, description = weather.description)
        weatherModelList.add(weatherModel)
    }
    return WeatherResponseModel(temperatureDetailsModel, weatherModelList)
}