package com.example.basicweatherapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicweatherapplication.BuildConfig
import com.example.basicweatherapplication.converters.convertToLocationDetailsModel
import com.example.basicweatherapplication.model.location.LocationModel
import com.example.basicweatherapplication.network.location.LocationRetrofitClient

class LocationViewModel : ViewModel() {
    private val _locationData = MutableLiveData<LocationModel?>()
    val locationData: LiveData<LocationModel?> = _locationData
    suspend fun fetchLocation(latitude: Double, longitude: Double, limit: Int) {
        try {
            val response = LocationRetrofitClient.apiService.getCity(latitude, longitude, limit, BuildConfig.API_KEY)
            _locationData.postValue(response[0].convertToLocationDetailsModel())
        } catch (exception: Exception) {
            _locationData.postValue(null)
        }
    }
}