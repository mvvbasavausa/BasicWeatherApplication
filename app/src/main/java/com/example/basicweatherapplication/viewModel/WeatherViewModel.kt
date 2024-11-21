package com.example.basicweatherapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicweatherapplication.BuildConfig
import com.example.basicweatherapplication.converters.convertToWeatherResponseModel
import com.example.basicweatherapplication.model.weather.WeatherResponseModel
import com.example.basicweatherapplication.network.weather.WeatherRetrofitClient

class WeatherViewModel : ViewModel() {
    private val _weatherData = MutableLiveData<WeatherResponseModel?>()
    val weatherData: LiveData<WeatherResponseModel?> = _weatherData
    suspend fun fetchWeather(city: String) {
        try {
            val response = WeatherRetrofitClient.apiService.getWeatherByCity(city, BuildConfig.API_KEY)
            _weatherData.postValue(response.convertToWeatherResponseModel())
        } catch (exception: Exception) {
            _weatherData.postValue(null)
        }
    }
}