package com.example.basicweatherapplication.model.location

import junit.framework.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class LocationModelTest {
    @Test
    fun `Ensure that the name field is correctly set and retrievable`() {
        val input = LocationModel(name = "New York")
        assertEquals("New York", input.name)
    }

    @Test
    fun `Verify the behavior of the equals method`() {
        val model1 = LocationModel(name = "Chicago")
        val model2 = LocationModel(name = "Chicago")
        val model3 = LocationModel(name = "Houston")
        assertEquals(model1, model2) // Same data
        assertNotEquals(model1, model3) // Different data
    }

    @Test
    fun `Ensure hashCode is consistent with equals`() {
        val model1 = LocationModel(name = "Miami")
        val model2 = LocationModel(name = "Miami")
        val model3 = LocationModel(name = "Seattle")
        assertEquals(model1.hashCode(), model2.hashCode()) // Same data
        assertNotEquals(model1.hashCode(), model3.hashCode())
    }

    @Test
    fun `Test the behavior when the name field is an empty string`() {
        val locationModel = LocationModel(name = "")
        assertEquals("", locationModel.name)
    }

    @Test
    fun `Ensure the name field can handle special characters`() {
        val locationModel = LocationModel(name = "São Paulo 🌟")
        assertEquals("São Paulo 🌟", locationModel.name)
    }

    @Test
    fun `Verify the behavior of the copy function in the LocationModel class`() {
        val original = LocationModel(name = "Paris")
        val copy = original.copy(name = "Berlin")
        assertEquals("Paris", original.name)
        assertEquals("Berlin", copy.name)
    }

    @Test
    fun `Verify behavior with long strings`() {
        val longName = "City".repeat(1_000)
        val locationModel = LocationModel(name = longName)
        assertEquals(longName, locationModel.name)
    }
}