package com.example.basicweatherapplication.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.basicweatherapplication.BuildConfig
import com.example.basicweatherapplication.model.weather.TemperatureDetailsModel
import com.example.basicweatherapplication.model.weather.WeatherModel
import com.example.basicweatherapplication.model.weather.WeatherResponseModel
import com.example.basicweatherapplication.network.weather.WeatherApiService
import com.example.basicweatherapplication.network.weather.WeatherRetrofitClient
import com.example.basicweatherapplication.network.weather.jsonData.Main
import com.example.basicweatherapplication.network.weather.jsonData.Weather
import com.example.basicweatherapplication.network.weather.jsonData.WeatherResponse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class WeatherViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // For LiveData testing
    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        //MockitoAnnotations.openMocks(this)
        MockitoAnnotations.initMocks(this)
        viewModel = spyk(WeatherViewModel())
    }

    @Test
    fun `fetchWeather should post weather data on success`() = runTest {
        val mockWeatherResponseModel = WeatherResponseModel(
            temperatureDetailsModel = TemperatureDetailsModel(temperature = 25.0, humidity = 60),
            weatherModelList = listOf(WeatherModel(id = 1, type = "Rain", icon = "10d", description = "light rain"))
        )
        val mockWeatherResponse = WeatherResponse(
            main = Main(temperature = 25.0, humidity = 60),
            weather = listOf(Weather(id = 1, main = "Rain", icon = "10d", description = "light rain"))
        )
        val mockApiService = mockk<WeatherApiService>()
        coEvery { mockApiService.getWeatherByCity("TestCity", BuildConfig.API_KEY) } returns mockWeatherResponse
        mockkObject(WeatherRetrofitClient)
        every { WeatherRetrofitClient.apiService } returns mockApiService
        val mockObserver = mockk<Observer<WeatherResponseModel?>>(relaxed = true)
        viewModel.weatherData.observeForever(mockObserver)
        viewModel.fetchWeather("TestCity")
        verify { mockObserver.onChanged(mockWeatherResponseModel) }
        viewModel.weatherData.removeObserver(mockObserver)
    }


    @Test
    fun `fetchWeather should post null when an exception occurs`() = runTest {
        val mockApiService = mockk<WeatherApiService>()
        coEvery { mockApiService.getWeatherByCity(any(), any()) } throws RuntimeException("Network error")
        mockkObject(WeatherRetrofitClient)
        every { WeatherRetrofitClient.apiService } returns mockApiService
        val mockObserver = mockk<Observer<WeatherResponseModel?>>(relaxed = true)
        viewModel.weatherData.observeForever(mockObserver)
        viewModel.fetchWeather("TestCity")
        verify { mockObserver.onChanged(null) }
        viewModel.weatherData.removeObserver(mockObserver)
    }

    @Test
    fun `weatherData should remain unchanged if fetchWeather is not called`() {
        val mockObserver = mockk<Observer<WeatherResponseModel?>>(relaxed = true)
        viewModel.weatherData.observeForever(mockObserver)
        verify(exactly = 0) { mockObserver.onChanged(any()) }
        viewModel.weatherData.removeObserver(mockObserver)
    }
}