package com.android.weatherui.weather.domain.models.weather_related

import com.android.weatherui.common.domain.extensions.takAmPmStringFromFormattedTimeString
import com.android.weatherui.common.domain.extensions.takeHoursFromFormattedTimeString
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class ForecastHour(
    val air_quality: WeatherAirQuality = WeatherAirQuality(),
    val chance_of_rain: Int = 0,
    val chance_of_snow: Int = 0,
    val cloud: Int = 0,
    val condition: WeatherCondition = WeatherCondition(),
    val dewpoint_c: Double = 0.0,
    val dewpoint_f: Double = 0.0,
    val feelslike_c: Double = 0.0,
    val feelslike_f: Double = 0.0,
    val gust_kph: Double = 0.0,
    val gust_mph: Double = 0.0,
    val heatindex_c: Double = 0.0,
    val heatindex_f: Double = 0.0,
    val humidity: Int = 0,
    val is_day: Int = 0,
    val precip_in: Double = 0.0,
    val precip_mm: Double = 0.0,
    val pressure_in: Double = 0.0,
    val pressure_mb: Double = 0.0,
    val snow_cm: Double = 0.0,
    val temp_c: Double = 0.0,
    val temp_f: Double = 0.0,
    val time: String = "",
    val time_epoch: Int = 0,
    val uv: Double = 0.0,
    val vis_km: Double = 0.0,
    val vis_miles: Double = 0.0,
    val will_it_rain: Int = 0,
    val will_it_snow: Int = 0,
    val wind_degree: Int = 0,
    val wind_dir: String = "",
    val wind_kph: Double = 0.0,
    val wind_mph: Double = 0.0,
    val windchill_c: Double = 0.0,
    val windchill_f: Double = 0.0,
)

fun ForecastHour.isCurrentForecastHour(
    formatedCurrentCityTime: String,
): Boolean {


    val hourlyForecastTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    val timeFormatter12Hours = DateTimeFormatter.ofPattern("hh:mm a")


    val forecastHoursLocalTime = try {
        LocalDateTime.parse(time, hourlyForecastTimeFormatter)
    } catch (e: Exception) {
        e.printStackTrace()
       return false
    }

    val currentLocalDateTime = LocalDateTime.now()

    val formattedForecastHoursTimeString = try {
        timeFormatter12Hours.format(forecastHoursLocalTime)
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }


    return formattedForecastHoursTimeString.takeHoursFromFormattedTimeString() ==
            formatedCurrentCityTime.takeHoursFromFormattedTimeString() &&
            formattedForecastHoursTimeString.takAmPmStringFromFormattedTimeString() ==
            formatedCurrentCityTime.takAmPmStringFromFormattedTimeString()

}