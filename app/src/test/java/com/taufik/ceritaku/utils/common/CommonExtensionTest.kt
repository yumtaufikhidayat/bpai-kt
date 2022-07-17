package com.taufik.ceritaku.utils.common

import org.junit.Assert
import org.junit.Test
import java.time.format.DateTimeParseException
import java.time.zone.ZoneRulesException

class CommonExtensionTest {
    @Test
    fun `given correct ISO 8601 format then should formatted correctly`() {
        val currentDate = "2022-07-17T00:27:43Z"
        Assert.assertEquals("17 July 2022 | 07:27", CommonExtension.formatDate(currentDate, "Asia/Jakarta"))
        Assert.assertEquals("17 July 2022 | 08:27", CommonExtension.formatDate(currentDate, "Asia/Makassar"))
        Assert.assertEquals("17 July 2022 | 09:27", CommonExtension.formatDate(currentDate, "Asia/Jayapura"))
    }

    @Test
    fun `given wrong ISO 8601 format then should throw error`() {
        val wrongFormat = "2022-07-17T00:27"
        Assert.assertThrows(DateTimeParseException::class.java) {
            CommonExtension.formatDate(wrongFormat, "Asia/Jakarta")
        }
    }

    @Test
    fun `given invalid timezone then should throw error`() {
        val wrongFormat = "2022-07-17T00:27:43Z"
        Assert.assertThrows(ZoneRulesException::class.java) {
            CommonExtension.formatDate(wrongFormat, "Asia/Makassar")
        }
    }
}