package com.example.basicweatherapplication.network.location

import com.example.basicweatherapplication.network.ApiConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LocationRetrofitClient {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.LOCATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: LocationApiService by lazy {
        retrofit.create(LocationApiService::class.java)
    }
}