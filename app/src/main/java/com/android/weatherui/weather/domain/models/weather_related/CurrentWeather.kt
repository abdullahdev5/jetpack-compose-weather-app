package com.android.weatherui.weather.domain.models.weather_related

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("cloud")
    val cloudCover: Int = 0,
    @SerializedName("condition")
    val condition: WeatherCondition = WeatherCondition(),
    @SerializedName("dewpoint_c")
    val dewpointCelsius: Double = 0.0,
    @SerializedName("dewpoint_f")
    val dewpointFahrenheit: Double = 0.0,
    @SerializedName("feelslike_c")
    val feelslikeCelsius: Double = 0.0,
    @SerializedName("feelslike_f")
    val feelslikeFahrenheit: Double = 0.0,
    @SerializedName("gust_kph")
    val gustKph: Double = 0.0,
    @SerializedName("gust_mph")
    val gustMph: Double = 0.0,
    @SerializedName("heatindex_c")
    val heatindexCelsius: Double = 0.0,
    @SerializedName("heatindex_f")
    val heatindexFahrenheit: Double = 0.0,
    @SerializedName("humidity")
    val humidity: Int = 0,
    @SerializedName("is_day")
    val isDay: Int = 2, // Day = 1, Night = 2
    @SerializedName("last_updated")
    val lastUpdated: String = "",
    @SerializedName("last_updated_epoch")
    val lastUpdatedEpoch: Int = 0,
    @SerializedName("precip_in")
    val precipitationInches: Double = 0.0,
    @SerializedName("precip_mm")
    val precipitationMillimeters: Double = 0.0,
    @SerializedName("pressure_in")
    val pressureInches: Double = 0.0,
    @SerializedName("pressure_mb")
    val pressureMillibars: Double = 0.0,
    @SerializedName("temp_c")
    val temperatureCelsius: Double = 0.0,
    @SerializedName("temp_f")
    val temperatureFahrenheit: Double = 0.0,
    @SerializedName("uv")
    val uvIndex: Double = 0.0,
    @SerializedName("vis_km")
    val visibilityKilometers: Double = 0.0,
    @SerializedName("vis_miles")
    val visibilityMiles: Double = 0.0,
    @SerializedName("wind_degree")
    val windDegree: Int = 0,
    @SerializedName("wind_dir")
    val windDirection: String = "",
    @SerializedName("wind_kph")
    val windKph: Double = 0.0,
    @SerializedName("wind_mph")
    val windMph: Double = 0.0, // Miles Per Hour
    @SerializedName("windchill_c")
    val windChillCelsius: Double = 0.0,
    @SerializedName("windchill_f")
    val windChillFahrenheit: Double = 0.0,
    @SerializedName("air_quality")
    val airQuality: WeatherAirQuality = WeatherAirQuality(),
)