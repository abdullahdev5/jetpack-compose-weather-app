package com.android.weatherui.weather.domain.models.weather_related

import com.google.gson.annotations.SerializedName

data class WeatherAlerts(
    @SerializedName("alert")
    val alert: List<WeatherAlert> = emptyList()
)