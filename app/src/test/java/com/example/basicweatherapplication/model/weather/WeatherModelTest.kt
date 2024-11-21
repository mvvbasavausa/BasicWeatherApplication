package com.example.basicweatherapplication.model.weather

import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherModelTest {
    @Test
    fun `Confirm that a WeatherModel object is correctly initialized with the expected values`() {
        val weatherModel = WeatherModel(id = 123, type = "Rain", icon = "10d", description = "light rain")
        assertEquals(123, weatherModel.id)
        assertEquals("Rain", weatherModel.type)
        assertEquals("10d", weatherModel.icon)
        assertEquals("light rain", weatherModel.description)
    }

    @Test
    fun `Verify that two WeatherModel objects with the same values are considered equal`() {
        val weatherModel1 = WeatherModel(id = 123, type = "Rain", icon = "10d", description = "light rain")
        val weatherModel2 = WeatherModel(id = 123, type = "Rain", icon = "10d", description = "light rain")
        assertEquals(weatherModel1, weatherModel2)
    }

    @Test
    fun `Ensure the WeatherModel class is serialized to JSON correctly`() {
        val weatherModel = WeatherModel(id = 123, type = "Rain", icon = "10d", description = "light rain")
        val json = Gson().toJson(weatherModel)
        assertTrue(json.contains("\"id\":123"))
        assertTrue(json.contains("\"type\":\"Rain\""))
        assertTrue(json.contains("\"icon\":\"10d\""))
        assertTrue(json.contains("\"description\":\"light rain\""))
    }

    @Test
    fun `Confirm that a WeatherModel object can be deserialized from a JSON string`() {
        val json = """
            {
                "id": 123,
                "type": "Rain",
                "icon": "10d",
                "description": "light rain"
            }
        """
        val weatherModel = Gson().fromJson(json, WeatherModel::class.java)
        assertEquals(123, weatherModel.id)
        assertEquals("Rain", weatherModel.type)
        assertEquals("10d", weatherModel.icon)
        assertEquals("light rain", weatherModel.description)
    }

    @Test
    fun `Ensure that the toString() method provides a meaningful representation of the object`() {
        val weatherModel = WeatherModel(id = 123, type = "Rain", icon = "10d", description = "light rain")
        val expectedOutput = "WeatherModel(id=123, type=Rain, icon=10d, description=light rain)"
        assertEquals(expectedOutput, weatherModel.toString())
    }
}