package com.android.weatherui.weather.domain.models.weather_related

import kotlinx.serialization.Serializable

@Serializable
data class WeatherAstronomy(
    val is_moon_up: Int = 0,
    val is_sun_up: Int = 0,
    val moon_illumination: Int = 0,
    val moon_phase: String = "",
    val moonrise: String = "",
    val moonset: String = "",
    val sunrise: String = "",
    val sunset: String = ""
)