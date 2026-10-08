package com.android.weatherui.weather.domain.models.weather_related

import com.google.gson.annotations.SerializedName

data class ForecastDay(
    @SerializedName("astro")
    val astro: WeatherAstronomy = WeatherAstronomy(),
    @SerializedName("date")
    val date: String = "",
    @SerializedName("date_epoch")
    val date_epoch: Int = 0,
    @SerializedName("day")
    val day: ForecastDayLight = ForecastDayLight(),
    @SerializedName("hour")
    val hour: List<ForecastHour> = emptyList()
)