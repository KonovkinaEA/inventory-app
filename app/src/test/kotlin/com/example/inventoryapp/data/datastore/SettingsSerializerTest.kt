package com.example.inventoryapp.data.datastore

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class SettingsSerializerTest {

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `get default value`() {
        assertEquals(UserSettings(), SettingsSerializer.defaultValue)
    }

    @Test
    fun `read from InputStream returns UserSettings`() = runBlocking {
        val json = """{"ipAddress":"address"}"""
        val inputStream = ByteArrayInputStream(json.toByteArray())

        val result = SettingsSerializer.readFrom(inputStream)

        assertEquals(UserSettings(ipAddress = "address"), result)
    }

    @Test
    fun `read from InputStream returns default value on SerializationException`() = runBlocking {
        val invalidJson = """{"invalid_json"}"""
        val inputStream = ByteArrayInputStream(invalidJson.toByteArray())

        val result = SettingsSerializer.readFrom(inputStream)

        assertEquals(SettingsSerializer.defaultValue, result)
    }

    @Test
    fun `write to OutputStream writes UserSettings`() = runBlocking {
        val userSettings = UserSettings(ipAddress = "address")
        val outputStream = ByteArrayOutputStream()

        SettingsSerializer.writeTo(userSettings, outputStream)

        val expectedJson = """{"ipAddress":"address"}"""
        assertEquals(expectedJson, outputStream.toString())
    }
}
