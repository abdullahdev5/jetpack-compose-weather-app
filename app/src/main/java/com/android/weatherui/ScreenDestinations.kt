package com.android.weatherui

import kotlinx.serialization.Serializable

sealed class ScreenDestinations {

    // Home Screen Destination
    @Serializable
    data object HOME_SCREEN_DESTINATION: ScreenDestinations()

    // Daily Forecast Screen Destination
    @Serializable
    data object DAILY_FORECAST_SCREEN_DESTINATION: ScreenDestinations()

    // Hourly Forecast Screen Destination
    @Serializable
    data class HourlyForecastScreenDestination(
        val currentCityFormattedTimeString: String,
        val isGoingForNextTwoHours: Boolean = false,
    ): ScreenDestinations()

    // Air Pollution Level Screen Destination
    @Serializable
    data class AirPollutionLevelScreenDestination(
        val airQualityLevel: Int
    ): ScreenDestinations()

    // Daily Details Screen Destination
    @Serializable
    data class DailyDetailsScreenDestination(
        val selectedForecastDayIndex: Int?
    ): ScreenDestinations()

    // City Selection Screen Destination
    @Serializable
    data class CitySelectionScreenDestination(
        val previousSelectedCity: String,
    ): ScreenDestinations()

}