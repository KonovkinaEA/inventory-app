package com.example.inventoryapp.data.datastore

import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsSerializerTest {

    @Test
    fun `get default value`() {
        assertEquals(UserSettings(), SettingsSerializer.defaultValue)
    }
}
