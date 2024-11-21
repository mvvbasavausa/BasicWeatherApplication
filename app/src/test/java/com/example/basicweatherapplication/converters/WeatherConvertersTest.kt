package com.example.basicweatherapplication.converters

import com.example.basicweatherapplication.network.weather.jsonData.Main
import com.example.basicweatherapplication.network.weather.jsonData.Weather
import com.example.basicweatherapplication.network.weather.jsonData.WeatherResponse
import junit.framework.Assert.assertEquals
import org.junit.Test

class WeatherConvertersTest {
    @Test
    fun `Verify Correct Mapping of Temperature Details`() {
        val input = WeatherResponse(main = Main(temperature = 25.0, humidity = 60), weather = listOf())
        val output = input.convertToWeatherResponseModel()
        assertEquals(25.0, output.temperatureDetailsModel.temperature)
        assertEquals(60, output.temperatureDetailsModel.humidity)
    }

    @Test
    fun `Verify Correct Mapping of Weather List`() {
        val input = WeatherResponse(main = Main(temperature = 20.0, humidity = 50),
            weather = listOf(
                Weather(id = 1, main = "Clouds", icon = "03d", description = "Cloudy"),
                Weather(id = 2, main = "Rain", icon = "10d", description = "Rainy")
            )
        )
        val output = input.convertToWeatherResponseModel()
        assertEquals(20.0, output.temperatureDetailsModel.temperature)
        assertEquals(50, output.temperatureDetailsModel.humidity)
        assertEquals(1, output.weatherModelList[0].id)
        assertEquals("Clouds", output.weatherModelList[0].type)
        assertEquals("03d", output.weatherModelList[0].icon)
        assertEquals("Cloudy", output.weatherModelList[0].description)
        assertEquals(2, output.weatherModelList[1].id)
        assertEquals("Rain", output.weatherModelList[1].type)
        assertEquals("10d", output.weatherModelList[1].icon)
        assertEquals("Rainy", output.weatherModelList[1].description)
    }

    @Test
    fun `Handle Empty Weather List`() {
        val input = WeatherResponse(main = Main(temperature = 15.0, humidity = 70), weather = listOf())
        val output = input.convertToWeatherResponseModel()
        assertEquals(15.0, output.temperatureDetailsModel.temperature)
        assertEquals(70, output.temperatureDetailsModel.humidity)
        assertEquals(0, output.weatherModelList.size)
    }

    @Test
    fun `Handle Default Values or Missing Fields`() {
        val input = WeatherResponse(main = Main(temperature = 0.0, humidity = 0), weather = listOf())
        val output = input.convertToWeatherResponseModel()
        assertEquals(0.0, output.temperatureDetailsModel.temperature)
        assertEquals(0, output.temperatureDetailsModel.humidity)
        assertEquals(0, output.weatherModelList.size)
    }

    @Test
    fun `Multiple Weather Objects with Same ID`() {
        val input = WeatherResponse(main = Main(temperature = 22.0, humidity = 65),
            weather = listOf(
                Weather(id = 1, main = "Clear", icon = "01d", description = "Sunny"),
                Weather(id = 1, main = "Clear", icon = "01d", description = "Sunny")
            )
        )
        val output = input.convertToWeatherResponseModel()
        assertEquals(22.0, output.temperatureDetailsModel.temperature)
        assertEquals(65, output.temperatureDetailsModel.humidity)
        assertEquals(1, output.weatherModelList[0].id)
        assertEquals("Clear", output.weatherModelList[0].type)
        assertEquals("01d", output.weatherModelList[0].icon)
        assertEquals("Sunny", output.weatherModelList[0].description)
        assertEquals(1, output.weatherModelList[1].id)
        assertEquals("Clear", output.weatherModelList[1].type)
        assertEquals("01d", output.weatherModelList[1].icon)
        assertEquals("Sunny", output.weatherModelList[1].description)
    }
}