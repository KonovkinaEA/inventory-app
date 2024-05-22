package com.example.inventoryapp.util

import org.junit.Assert.assertEquals
import org.junit.Test

class DateFormattersTest {

    @Test
    fun `convert unix timestamp to string date`() {
        assertEquals("01.01.1970", 0L.millisToDate())
    }
}
