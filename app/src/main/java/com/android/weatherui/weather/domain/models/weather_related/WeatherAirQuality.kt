package com.android.weatherui.weather.domain.models.weather_related

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherAirQuality(
    @SerializedName("co")
    val co: Double = 0.0,
    @SerializedName("no2")
    val no2: Double = 0.0,
    @SerializedName("o3")
    val o3: Double = 0.0,
    @SerializedName("so2")
    val so2: Double = 0.0,
    @SerializedName("pm2_5")
    val pm2_5: Double = 0.0,
    @SerializedName("pm10")
    val pm10: Double = 0.0,
    @SerializedName("us-epa-index")
    val us_epa_index: Int = 0,
    @SerializedName("gb-defra-index")
    val gb_defra_index: Int = 0,
)