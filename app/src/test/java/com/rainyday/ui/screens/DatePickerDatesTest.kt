/*
 * Note: Generated and refined with AI assistance.
 */
package com.rainyday.ui.screens

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.util.TimeZone

/**
 * Verifies the start-date conversions used by the transaction dialog's DatePicker.
 */
class DatePickerDatesTest {

    private val originalZone: TimeZone = TimeZone.getDefault()

    @After
    fun restoreZone() {
        TimeZone.setDefault(originalZone)
    }

    @Test
    fun roundTrip_keepsTheSameDay_inZonesEitherSideOfUtc() {
        val dates = listOf(
            LocalDate.of(2026, 11, 21),
            LocalDate.of(2026, 12, 31),
            LocalDate.of(2028, 2, 29),
            LocalDate.of(2027, 3, 14), // US daylight-saving change
        )
        for (zone in listOf("America/Chicago", "Pacific/Kiritimati", "Pacific/Pago_Pago", "UTC")) {
            TimeZone.setDefault(TimeZone.getTimeZone(zone))
            for (date in dates) {
                assertEquals("$date in $zone", date, datePickerMillisToLocalDate(date.toDatePickerMillis()))
            }
        }
    }

    @Test
    fun toDatePickerMillis_isMidnightUtc() {
        // 2026-11-21T00:00:00Z
        assertEquals(1_795_219_200_000L, LocalDate.of(2026, 11, 21).toDatePickerMillis())
    }
}
