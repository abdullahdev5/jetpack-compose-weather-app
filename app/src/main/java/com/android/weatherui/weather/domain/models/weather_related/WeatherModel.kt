package com.android.weatherui.weather.domain.models.weather_related

import com.google.gson.annotations.SerializedName


data class WeatherModel(
    @SerializedName("alerts")
    val alerts: WeatherAlerts = WeatherAlerts(),
    @SerializedName("current")
    val current: CurrentWeather = CurrentWeather(),
    @SerializedName("forecast")
    val forecast: WeatherForecast = WeatherForecast(),
    @SerializedName("location")
    val location: WeatherLocation = WeatherLocation(),
)