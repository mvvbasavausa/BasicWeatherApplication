package com.example.basicweatherapplication.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.basicweatherapplication.BuildConfig
import com.example.basicweatherapplication.converters.convertToLocationDetailsModel
import com.example.basicweatherapplication.model.location.LocationModel
import com.example.basicweatherapplication.network.location.LocationApiService
import com.example.basicweatherapplication.network.location.LocationRetrofitClient
import com.example.basicweatherapplication.network.location.jsonData.LocalNames
import com.example.basicweatherapplication.network.location.jsonData.LocationItem
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
class LocationViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // For LiveData testing
    private lateinit var viewModel: LocationViewModel

    @Before
    fun setUp() {
        //MockitoAnnotations.openMocks(this)
        MockitoAnnotations.initMocks(this)
        viewModel = spyk(LocationViewModel())
    }

    @Test
    fun `fetchLocation should post location data on success`() = runTest {
        val locationModel = LocationModel("name")
        mockk<LocationItem> {
            every { convertToLocationDetailsModel() } returns locationModel
        }
        val mockListLocationItem = listOf(LocationItem(country = "country",
            lat = 10.0, lon = 10.0, localNames = LocalNames(en = "en", sr = "sr"),
            name = "name", state = "state"
        ))
        val mockApiService = mockk<LocationApiService>()
        coEvery { mockApiService.getCity(10.0, 10.0, 5, BuildConfig.API_KEY) } returns mockListLocationItem
        mockkObject(LocationRetrofitClient)
        every { LocationRetrofitClient.apiService } returns mockApiService
        val mockObserver = mockk<Observer<LocationModel?>>(relaxed = true)
        viewModel.locationData.observeForever(mockObserver)
        viewModel.fetchLocation(10.0, 10.0, 5)
        verify { mockObserver.onChanged(locationModel) }
        viewModel.locationData.removeObserver(mockObserver)
    }


    @Test
    fun `fetchWeather should post null when an exception occurs`() = runTest {
        val mockApiService = mockk<LocationApiService>()
        coEvery { mockApiService.getCity(any(), any(), any(), any()) } throws RuntimeException("Network error")
        mockkObject(LocationRetrofitClient)
        every { LocationRetrofitClient.apiService } returns mockApiService
        val mockObserver = mockk<Observer<LocationModel?>>(relaxed = true)
        viewModel.locationData.observeForever(mockObserver)
        viewModel.fetchLocation(10.0, 10.0, 5)
        verify { mockObserver.onChanged(null) }
        viewModel.locationData.removeObserver(mockObserver)
    }

    @Test
    fun `locationData should remain unchanged if fetchLocation is not called`() {
        val mockObserver = mockk<Observer<LocationModel?>>(relaxed = true)
        viewModel.locationData.observeForever(mockObserver)
        verify(exactly = 0) { mockObserver.onChanged(any()) }
        viewModel.locationData.removeObserver(mockObserver)
    }
}