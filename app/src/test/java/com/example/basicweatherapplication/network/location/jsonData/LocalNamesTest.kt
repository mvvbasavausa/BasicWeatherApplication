package com.example.basicweatherapplication.network.location.jsonData

import com.google.gson.Gson
import junit.framework.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class LocalNamesTest {
    @Test
    fun `Ensure that the fields en and sr are correctly set and retrievable`() {
        val localNames = LocalNames(en = "EnglishName", sr = "SerbianName")
        assertEquals("EnglishName", localNames.en)
        assertEquals("SerbianName", localNames.sr)
    }

    @Test
    fun `Verify that the class is correctly deserialized from a JSON string using Gson`() {
        val json = """{"en":"London","sr":"Лондон"}"""
        val localNames = Gson().fromJson(json, LocalNames::class.java)
        assertEquals("London", localNames.en)
        assertEquals("Лондон", localNames.sr)
    }

    @Test
    fun `Verify that the class is correctly serialized into a JSON string using Gson`() {
        val localNames = LocalNames(en = "Paris", sr = "Париз")
        val json = Gson().toJson(localNames)
        assertEquals("""{"en":"Paris","sr":"Париз"}""", json)
    }

    @Test
    fun `Ensure the behavior of the equals method for comparing objects with identical values`() {
        val obj1 = LocalNames(en = "Tokyo", sr = "Токио")
        val obj2 = LocalNames(en = "Tokyo", sr = "Токио")
        val obj3 = LocalNames(en = "Kyoto", sr = "Кјото")
        assertEquals(obj1, obj2) // Same values
        assertNotEquals(obj1, obj3) // Different values
    }

    @Test
    fun `Verify that hashCode is consistent with equals`() {
        val obj1 = LocalNames(en = "Berlin", sr = "Берлин")
        val obj2 = LocalNames(en = "Berlin", sr = "Берлин")
        val obj3 = LocalNames(en = "Munich", sr = "Минхен")
        assertEquals(obj1.hashCode(), obj2.hashCode()) // Same values
        assertNotEquals(obj1.hashCode(), obj3.hashCode()) // Different values
    }

    @Test
    fun `Verify behavior when en and sr fields are empty strings`() {
        val localNames = LocalNames(en = "", sr = "")
        assertEquals("", localNames.en)
        assertEquals("", localNames.sr)
    }

    @Test
    fun `Ensure the en and sr fields handle special characters`() {
        val localNames = LocalNames(en = "São Paulo 🌟", sr = "Сан Пауло 🌟")
        assertEquals("São Paulo 🌟", localNames.en)
        assertEquals("Сан Пауло 🌟", localNames.sr)
    }

    @Test
    fun `Verify behavior with long strings`() {
        val largeString = "CityName".repeat(10_000)
        val localNames = LocalNames(en = largeString, sr = largeString)
        assertEquals(largeString, localNames.en)
        assertEquals(largeString, localNames.sr)
    }

    @Test
    fun `Confirm the fields are correctly serialized with the specified @SerializedName annotations`() {
        val localNames = LocalNames(en = "Madrid", sr = "Мадрид")
        val json = Gson().toJson(localNames)
        assertEquals("""{"en":"Madrid","sr":"Мадрид"}""", json)
    }
}