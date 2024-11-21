package com.example.basicweatherapplication.converters

import com.example.basicweatherapplication.network.location.jsonData.LocalNames
import com.example.basicweatherapplication.network.location.jsonData.LocationItem
import junit.framework.Assert.assertEquals
import org.junit.Test

class LocationDetailsConvertersTest {
    @Test
    fun `Ensure the name field is correctly transferred from LocationItem to LocationModel`() {
        val input = LocationItem(name = "New York", localNames = LocalNames())
        val output = input.convertToLocationDetailsModel()
        assertEquals("New York", output.name)
    }

    @Test
    fun `Verify that the function handles an empty string as the name`() {
        val input = LocationItem(name = "", localNames = LocalNames())
        val output = input.convertToLocationDetailsModel()
        assertEquals("", output.name)
    }

    @Test
    fun `Ensure special characters in name are preserved`() {
        val input = LocationItem(name = "São Paulo! 🌟", localNames = LocalNames())
        val output = input.convertToLocationDetailsModel()
        assertEquals("São Paulo! \uD83C\uDF1F", output.name)
    }

    @Test
    fun `Verify that repeated calls to convertToLocationDetailsModel produce consistent results`() {
        val input = LocationItem(name = "Paris", localNames = LocalNames())
        val output1 = input.convertToLocationDetailsModel()
        assertEquals("Paris", output1.name)
        val output2 = input.convertToLocationDetailsModel()
        assertEquals("Paris", output2.name)
    }
}