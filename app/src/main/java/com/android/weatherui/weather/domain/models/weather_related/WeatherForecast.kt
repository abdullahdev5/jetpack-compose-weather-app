package com.android.weatherui.weather.domain.models.weather_related

import com.google.gson.annotations.SerializedName

data class WeatherForecast(
    @SerializedName("forecastday")
    val forecastDay: List<ForecastDay> = emptyList()
)