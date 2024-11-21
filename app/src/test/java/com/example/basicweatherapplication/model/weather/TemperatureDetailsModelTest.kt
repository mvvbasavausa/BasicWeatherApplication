package com.example.basicweatherapplication.model.weather

import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TemperatureDetailsModelTest {
    @Test
    fun `Confirm that a WeatherModel object is correctly initialized with the expected values`() {
        val temperatureDetailsModel = TemperatureDetailsModel(temperature = 25.5, humidity = 60)
        assertEquals(25.5, temperatureDetailsModel.temperature)
        assertEquals(60, temperatureDetailsModel.humidity)
    }

    @Test
    fun `Check if two instances of TemperatureDetailsModel with the same values are considered equal`() {
        val temperatureDetailsModel1 = TemperatureDetailsModel(temperature = 30.0, humidity = 70)
        val temperatureDetailsModel2 = TemperatureDetailsModel(temperature = 30.0, humidity = 70)
        assertEquals(temperatureDetailsModel1, temperatureDetailsModel2)
    }

    @Test
    fun `Validate hashCode consistency`() {
        val model1 = TemperatureDetailsModel(temperature = 20.0, humidity = 50)
        val model2 = TemperatureDetailsModel(temperature = 20.0, humidity = 50)
        assertEquals(model1.hashCode(), model2.hashCode())
    }

    @Test
    fun `Ensure that the string representation of the object is correct`() {
        val model = TemperatureDetailsModel(temperature = 22.0, humidity = 40)
        val expectedString = "TemperatureDetailsModel(temperature=22.0, humidity=40)"
        assertEquals(expectedString, model.toString())
    }

    @Test
    fun `Ensure the TemperatureDetailsModel class is serialized to JSON correctly`() {
        val temperatureDetailsModel = TemperatureDetailsModel(temperature = 25.5, humidity = 60)
        val json = Gson().toJson(temperatureDetailsModel)
        assertTrue(json.contains("\"temperature\":25.5"))
        assertTrue(json.contains("\"humidity\":60"))
    }

    @Test
    fun `Confirm that a TemperatureDetailsModel object can be deserialized from a JSON string`() {
        val json = """
            {
                "temperature": 25.5,
                "humidity": 60
            }
        """
        val temperatureDetailsModel = Gson().fromJson(json, TemperatureDetailsModel::class.java)
        assertEquals(25.5, temperatureDetailsModel.temperature)
        assertEquals(60, temperatureDetailsModel.humidity)
    }

    @Test
    fun `Ensure that the toString() method provides a meaningful representation of the object`() {
        val temperatureDetailsModel = TemperatureDetailsModel(temperature = 25.5, humidity = 60)
        val expectedOutput = "TemperatureDetailsModel(temperature=25.5, humidity=60)"
        assertEquals(expectedOutput, temperatureDetailsModel.toString())
    }
}