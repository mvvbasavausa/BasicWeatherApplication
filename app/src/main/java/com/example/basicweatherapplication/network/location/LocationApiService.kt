package com.example.basicweatherapplication.network.location

import com.example.basicweatherapplication.network.location.jsonData.LocationItem
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApiService {
    @GET("reverse")
    suspend fun getCity(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String
    ): List<LocationItem>
}