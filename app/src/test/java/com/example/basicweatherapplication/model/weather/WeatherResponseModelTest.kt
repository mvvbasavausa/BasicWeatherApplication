package com.example.basicweatherapplication.model.weather

import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherResponseModelTest {
    @Test
    fun `Confirm that a TemperatureDetailsModel object is correctly initialized with the expected values`() {
        val temperatureDetails = TemperatureDetailsModel(temperature = 30.0, humidity = 70)
        val weatherModels = listOf(
            WeatherModel(id = 3, type = "Snow", icon = "13d", description = "light snow")
        )
        val weatherResponseModel = WeatherResponseModel(temperatureDetails, weatherModels)
        assertEquals(30.0, weatherResponseModel.temperatureDetailsModel.temperature)
        assertEquals(70, weatherResponseModel.temperatureDetailsModel.humidity)
        assertEquals(3, weatherResponseModel.weatherModelList[0].id)
        assertEquals("Snow", weatherResponseModel.weatherModelList[0].type)
        assertEquals("13d", weatherResponseModel.weatherModelList[0].icon)
        assertEquals("light snow", weatherResponseModel.weatherModelList[0].description)
    }

    @Test
    fun `Verify that the hashCode for two equivalent objects is the same`() {
        val temperatureDetails = TemperatureDetailsModel(temperature = 30.0, humidity = 70)
        val weatherModels = listOf(WeatherModel(id = 3, type = "Snow", icon = "13d", description = "light snow"))
        val model1 = WeatherResponseModel(temperatureDetails, weatherModels)
        val model2 = WeatherResponseModel(temperatureDetails, weatherModels)
        assertEquals(model1.hashCode(), model2.hashCode())
    }

    @Test
    fun `Ensure that the toString method provides a correct representation of the object`() {
        val temperatureDetails = TemperatureDetailsModel(temperature = 18.0, humidity = 40)
        val weatherModels = listOf(
            WeatherModel(id = 5, type = "Fog", icon = "50d", description = "foggy")
        )
        val weatherResponseModel = WeatherResponseModel(temperatureDetails, weatherModels)
        val expectedString = "WeatherResponseModel(temperatureDetailsModel=TemperatureDetailsModel(temperature=18.0, humidity=40), weatherModelList=[WeatherModel(id=5, type=Fog, icon=50d, description=foggy)])"
        assertEquals(expectedString, weatherResponseModel.toString())
    }

    @Test
    fun `Ensure two instances with the same data are considered equal`() {
        val temperatureDetails = TemperatureDetailsModel(temperature = 22.0, humidity = 50)
        val weatherModels = listOf(WeatherModel(id = 1, type = "Sunny", icon = "01d", description = "clear sky"))
        val model1 = WeatherResponseModel(temperatureDetails, weatherModels)
        val model2 = WeatherResponseModel(temperatureDetails, weatherModels)
        assertEquals(model1, model2)
    }

    @Test
    fun `Ensure the WeatherModel class is serialized to JSON correctly`() {
        val temperatureDetails = TemperatureDetailsModel(temperature = 22.0, humidity = 50)
        val weatherModels = listOf(WeatherModel(id = 123, type = "Rain", icon = "10d", description = "light rain"))
        val weatherResponseModel = WeatherResponseModel(temperatureDetails, weatherModels)
        val json = Gson().toJson(weatherResponseModel)
        assertTrue(json.contains("\"temperature\":22.0"))
        assertTrue(json.contains("\"humidity\":50"))
        assertTrue(json.contains("\"id\":123"))
        assertTrue(json.contains("\"type\":\"Rain\""))
        assertTrue(json.contains("\"icon\":\"10d\""))
        assertTrue(json.contains("\"description\":\"light rain\""))
    }

    @Test
    fun `Confirm that a WeatherResponseModel object can be deserialized from a JSON string`() {
        val json = """
            {
                "temperatureDetailsModel": {
                    "temperature": 25.2,
                    "humidity": 12
                },
                "weatherModelList":
                    [
                        {
                            "id": 123,
                            "type": "Rain",
                            "icon": "10d",
                            "description": "light rain"
                        }
                    ]
            }
        """
        val weatherResponseModel = Gson().fromJson(json, WeatherResponseModel::class.java)
        assertEquals(25.2, weatherResponseModel.temperatureDetailsModel.temperature)
        assertEquals(12, weatherResponseModel.temperatureDetailsModel.humidity)
        assertEquals(123, weatherResponseModel.weatherModelList[0].id)
        assertEquals("Rain", weatherResponseModel.weatherModelList[0].type)
        assertEquals("10d", weatherResponseModel.weatherModelList[0].icon)
        assertEquals("light rain", weatherResponseModel.weatherModelList[0].description)
    }
}