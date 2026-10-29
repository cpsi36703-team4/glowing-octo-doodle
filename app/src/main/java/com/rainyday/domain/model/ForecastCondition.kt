package com.rainyday.domain.model

enum class ForecastCondition(val displayName: String, val description: String) {
    CLEAR("Clear", "Strong buffer, no heavy charges ahead"),
    FAIR("Fair", "Healthy balance above comfort floor"),
    CLOUDY("Cloudy", "Approaching comfort floor threshold"),
    RAIN("Rain", "Projected balance below comfort floor"),
    STORM("Storm", "Projected deficit or negative balance")
}
