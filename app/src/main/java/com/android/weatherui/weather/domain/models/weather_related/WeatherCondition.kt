package com.android.weatherui.weather.domain.models.weather_related


data class WeatherCondition(
    val code: Int = 0,
    val icon: String = "",
    val text: String = "Unknown"
)