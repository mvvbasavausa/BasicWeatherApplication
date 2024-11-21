package com.example.basicweatherapplication.network.location.jsonData

import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationItemTest {
    @Test
    fun `Ensure that a LocationItem object can be initialized correctly with all its properties`() {
        val localNames = LocalNames(en = "New York", sr = "Нови Сад")
        val locationItem = LocationItem(country = "USA", lat = 40.7128, lon = -74.0060,
            localNames = localNames, name = "New York", state = "New York")
        assertEquals("USA", locationItem.country)
        assertEquals(40.7128, locationItem.lat, 0.0)
        assertEquals(-74.0060, locationItem.lon, 0.0)
        assertEquals(localNames, locationItem.localNames)
        assertEquals("New York", locationItem.name)
        assertEquals("New York", locationItem.state)
    }

    @Test
    fun `Ensure the LocationItem class is correctly serialized into JSON format`() {
        val localNames = LocalNames(en = "New York", sr = "Нови Сад")
        val locationItem = LocationItem(country = "USA", lat = 40.7128, lon = -74.0060,
            localNames = localNames, name = "New York", state = "New York")
        val json = Gson().toJson(locationItem)
        assertTrue(json.contains("\"country\":\"USA\""))
        assertTrue(json.contains("\"lat\":40.7128"))
        assertTrue(json.contains("\"lon\":-74.006"))
        assertTrue(json.contains("\"name\":\"New York\""))
        assertTrue(json.contains("\"state\":\"New York\""))
        assertTrue(json.contains("\"local_names\":{\"en\":\"New York\",\"sr\":\"Нови Сад\"}"))
    }

    @Test
    fun `Ensure the LocationItem class can be deserialized from JSON format`() {
        val json = """
            {
                "country": "USA",
                "lat": 40.7128,
                "lon": -74.0060,
                "local_names": { "en": "New York", "sr": "Нови Сад" },
                "name": "New York",
                "state": "New York"
            }
        """
        val locationItem = Gson().fromJson(json, LocationItem::class.java)
        assertEquals("USA", locationItem.country)
        assertEquals(40.7128, locationItem.lat, 0.0)
        assertEquals(-74.0060, locationItem.lon, 0.0)
        assertEquals("New York", locationItem.localNames.en)
        assertEquals("Нови Сад", locationItem.localNames.sr)
        assertEquals("New York", locationItem.name)
        assertEquals("New York", locationItem.state)
    }

    @Test
    fun `Validate behavior when optional fields are missing in the JSON input`() {
        val json = """
            {
                "country": "USA",
                "lat": 40.7128,
                "lon": -74.0060,
                "name": "New York"
            }
        """
        val locationItem = Gson().fromJson(json, LocationItem::class.java)
        assertEquals("USA", locationItem.country)
        assertEquals(40.7128, locationItem.lat, 0.0)
        assertEquals(-74.0060, locationItem.lon, 0.0)
        assertNull(locationItem.localNames) // Assuming LocalNames is nullable.
        assertEquals("New York", locationItem.name)
        assertNull(locationItem.state)
    }

    @Test
    fun `Ensure two LocationItem objects with the same properties are considered equal`() {
        val localNames1 = LocalNames(en = "New York", sr = "Нови Сад")
        val locationItem1 = LocationItem(country = "USA", lat = 40.7128, lon = -74.0060,
            localNames = localNames1, name = "New York", state = "New York")
        val localNames2 = LocalNames(en = "New York", sr = "Нови Сад")
        val locationItem2 = LocationItem(country = "USA", lat = 40.7128, lon = -74.0060,
            localNames = localNames2, name = "New York", state = "New York")
        assertEquals(locationItem1, locationItem2)
    }

    @Test
    fun `Ensure the toString() method returns a properly formatted string representation`() {
        val localNames = LocalNames(en = "New York", sr = "Нови Сад")
        val locationItem = LocationItem(country = "USA", lat = 40.7128, lon = -74.0060,
            localNames = localNames, name = "New York", state = "New York")
        val expectedString = "LocationItem(country=USA, lat=40.7128, lon=-74.006, localNames=LocalNames(en=New York, sr=Нови Сад), name=New York, state=New York)"
        assertEquals(expectedString, locationItem.toString())
    }
}