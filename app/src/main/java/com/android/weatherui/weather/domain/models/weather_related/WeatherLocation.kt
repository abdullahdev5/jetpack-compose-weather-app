package com.android.weatherui.weather.domain.models.weather_related

import com.google.gson.annotations.SerializedName

data class WeatherLocation(
    @SerializedName("name")
    val city: String = "",
    @SerializedName("region")
    val region: String = "",
    @SerializedName("country")
    val country: String = "",
    @SerializedName("lat")
    val latitude: Double = 0.0,
    @SerializedName("lon")
    val longitude: Double = 0.0,
    @SerializedName("localtime")
    val localtime: String = "",
    @SerializedName("localtime_epoch")
    val localtime_epoch: Int = 0,
    @SerializedName("tz_id")
    val timezoneId: String = "",
)