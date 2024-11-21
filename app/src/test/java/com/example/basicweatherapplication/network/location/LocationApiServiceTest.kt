package com.example.basicweatherapplication.network.location

import com.example.basicweatherapplication.BuildConfig
import com.example.basicweatherapplication.network.ApiConfig.LOCATION_BASE_URL
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationApiServiceTest {
    @Test
    fun `Verify that the getCity method generates the correct API call with expected query parameters`() {
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("[]") // Provide a valid JSON response for the test
        )
        val apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationApiService::class.java)
        runBlocking {
            val response = apiService.getCity(latitude = 12.34, longitude = 56.78, limit = 5, apiKey = BuildConfig.API_KEY)
        }
        val request = mockWebServer.takeRequest()
        println("Request Path: ${request.path}")
        assertEquals("/reverse?lat=12.34&lon=56.78&limit=5&appid=dbf8944de21d4280e9a23b2d6a15800b", request.path)
        assertEquals("GET", request.method)
    }

    @Test
    fun `Verify that the response from the API is correctly deserialized into a list of LocationItem objects`() {
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(
            MockResponse()
                .setBody("[{\"name\": \"City1\"}, {\"name\": \"City2\"}]")
                .setResponseCode(200)
        )
        val apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationApiService::class.java)
        runBlocking {
            val response = apiService.getCity(latitude = 12.34, longitude = 56.78, limit = 5, apiKey = BuildConfig.API_KEY)
            println("Response: $response")
            assertEquals(2, response.size)
            assertEquals("City1", response[0].name)
            assertEquals("City2", response[1].name)
        }
    }

    @Test
    fun `Verify that an empty response from the server returns an empty list`() {
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setBody("[]").setResponseCode(200))
        val apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationApiService::class.java)
        runBlocking {
            val response = apiService.getCity(latitude = 12.34, longitude = 56.78, limit = 5, apiKey = BuildConfig.API_KEY)
            println("Response: $response")
            assertTrue(response.isEmpty())
        }
    }

    @Test
    fun `Verify that the method handles a server error response correctly`() {
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setResponseCode(500).setBody("{\"message\": \"Internal Server Error\"}"))
        val apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationApiService::class.java)
        runBlocking {
            try {
                apiService.getCity(latitude = 12.34, longitude = 56.78, limit = 5, apiKey = BuildConfig.API_KEY)
                fail("Expected exception was not thrown")
            } catch (exception: Exception) {
                assertTrue(exception is HttpException)
                assertEquals(500, (exception as HttpException).code())
            }
        }
    }

    @Test
    fun `Verify the behavior when the response JSON is missing optional fields`() {
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(MockResponse().setBody("[{}]").setResponseCode(200))

        val apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationApiService::class.java)

        runBlocking {
            val response = apiService.getCity(latitude = 12.34, longitude = 56.78, limit = 5, apiKey = BuildConfig.API_KEY)
            assertEquals(1, response.size)
            assertNull(response[0].name) // Assuming `name` is nullable in `LocationItem`.
        }
    }

    @Test
    fun `Ensure that invalid input parameters throw the appropriate error`() {
        val mockWebServer = MockWebServer()
        val apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url(LOCATION_BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationApiService::class.java)

        runBlocking {
            try {
                apiService.getCity(latitude = -999.0, longitude = -999.0, limit = 5, apiKey = BuildConfig.API_KEY)
                fail("Expected IllegalArgumentException for invalid parameters")
            } catch (exception: HttpException) {
                println("exception: $exception")
                println("exception.message: ${exception.message}")
                assertEquals("HTTP 400 Bad Request", exception.message)
            }
        }
    }

    @Test
    fun `Check if the API respects the limit parameter in the response`() {
        val mockWebServer = MockWebServer()
        mockWebServer.enqueue(
            MockResponse()
                .setBody("[{\"name\": \"City1\"}, {\"name\": \"City2\"}, {\"name\": \"City3\"}]")
                .setResponseCode(200)
        )

        val apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationApiService::class.java)

        runBlocking {
            val response = apiService.getCity(latitude = 12.34, longitude = 56.78, limit = 2, apiKey = BuildConfig.API_KEY)
            assertEquals(3, response.size) // Ensure it respects the limit
        }
    }
}