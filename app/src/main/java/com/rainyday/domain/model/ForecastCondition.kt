/*
 * Note: Generated and refined with AI assistance.
 */
package com.rainyday.domain.model

/**
 * Represents the daily financial weather outlook condition.
 *
 * @property displayName User-friendly label for the weather state.
 * @property description Detailed explanation of the financial outlook state.
 */
enum class ForecastCondition(val displayName: String, val description: String) {
    CLEAR("Clear", "Strong buffer, no heavy charges ahead"),
    FAIR("Fair", "Healthy balance above comfort floor"),
    CLOUDY("Cloudy", "Approaching comfort floor threshold"),
    RAIN("Rain", "Projected balance below comfort floor"),
    STORM("Storm", "Projected deficit or negative balance"),
}
