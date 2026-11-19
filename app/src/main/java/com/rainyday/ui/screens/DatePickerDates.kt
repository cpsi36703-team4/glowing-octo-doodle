/*
 * Note: Generated and refined with AI assistance.
 */
package com.rainyday.ui.screens

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/**
 * Conversions between [LocalDate] and the epoch milliseconds used by the Material 3 DatePicker.
 *
 * The DatePicker represents a selected day as midnight UTC of that day, regardless of the
 * device's time zone. Converting through the device zone instead would shift the date by a day
 * for anyone west of UTC (e.g. picking Nov 21 in Central time would save Nov 20), so both
 * directions deliberately use UTC.
 */
internal fun LocalDate.toDatePickerMillis(): Long =
    atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

/**
 * Converts a DatePicker selection (midnight UTC, in epoch milliseconds) back to a [LocalDate].
 */
internal fun datePickerMillisToLocalDate(millis: Long): LocalDate =
    Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
