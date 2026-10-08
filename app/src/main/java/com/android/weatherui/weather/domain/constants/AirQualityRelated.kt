package com.android.weatherui.weather.domain.constants

import com.android.weatherui.R
import com.android.weatherui.ui.theme.Brown
import com.android.weatherui.ui.theme.Green
import com.android.weatherui.ui.theme.Orange
import com.android.weatherui.ui.theme.Purple
import com.android.weatherui.ui.theme.Red
import com.android.weatherui.ui.theme.Yellow

class AirQualityRelated {

    companion object {

        fun getAirQualityLevel(airQuality: Int) = when {
            airQuality <= 50 -> GOOD
            airQuality <= 100 -> MODERATE
            airQuality <= 150 -> SENSITIVE
            airQuality <= 200 -> UNHEALTHY
            airQuality <= 300 -> VERY_UNHEALTHY
            else -> HAZARDOUS
        }

        fun getAirQualityLevelDescription(airQuality: Int) = when {
            airQuality <= 50 -> GOOD_DESC
            airQuality <= 100 -> MODERATE_DESC
            airQuality <= 150 -> SENSITIVE_DESC
            airQuality <= 200 -> UNHEALTHY_DESC
            airQuality <= 300 -> VERY_UNHEALTHY_DESC
            else -> HAZARDOUS_DESC
        }

        fun getAirQualityLevelColor(airQuality: Int) = when {
            airQuality <= 50 -> Green
            airQuality <= 100 -> Yellow
            airQuality <= 150 -> Orange
            airQuality <= 200 -> Red
            airQuality <= 300 -> Purple
            else -> Brown
        }

        fun getAirQualityLevelRangeFromLevel(airQuality: Int) = when {
            airQuality <= 50 -> "0-50"
            airQuality <= 100 -> "50-100"
            airQuality <= 150 -> "100-150"
            airQuality <= 200 -> "150-200"
            airQuality <= 300 -> "200-300"
            else -> "300-400"
        }

        fun getAirQualityLevelIconResId(airQuality: Int) = when {
            airQuality <= 50 -> R.drawable.air_quality_good_icon
            airQuality <= 100 -> R.drawable.air_quality_moderate_icon
            airQuality <= 150 -> R.drawable.air_quality_sensitive_icon
            airQuality <= 200 -> R.drawable.air_quality_unhealthy_icon
            airQuality <= 300 -> R.drawable.air_quality_very_unhealthy_icon
            else -> R.drawable.air_quality_hazardous_icon
        }

//        object AirQualityLevels {

        // Good
        const val GOOD = "Good"

        // Moderate
        const val MODERATE = "Moderate"

        // Sensitive
        const val SENSITIVE = "Sensitive"

        // Un Healthy
        const val UNHEALTHY = "Unhealthy"

        // Very UnHealthy
        const val VERY_UNHEALTHY = "Very Unhealthy"

        // Hazardous
        const val HAZARDOUS = "Hazardous"

//        }

//        object AirQualityLevelsDescription {

        // Good
        const val GOOD_DESC = "Air Quality is Good."

        // Moderate
        const val MODERATE_DESC = "Air Quality is Moderate."

        // Sensitive
        const val SENSITIVE_DESC = "Air Quality is Unhealthy for Sensitive Groups."

        // Un Healthy
        const val UNHEALTHY_DESC = "Air Quality is Unhealthy."

        // Very UnHealthy
        const val VERY_UNHEALTHY_DESC = "Air Quality is Very Unhealthy."

        // Hazardous
        const val HAZARDOUS_DESC = "Air Quality is Hazardous."

//        }


    }

}