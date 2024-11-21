package com.example.basicweatherapplication.network.weather

import com.example.basicweatherapplication.network.weather.jsonData.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): WeatherResponse
}