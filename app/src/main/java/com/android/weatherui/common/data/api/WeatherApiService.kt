package com.android.weatherui.common.data.api

import com.android.weatherui.weather.domain.models.weather_related.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {


    companion object {
        private const val WEATHER_API = BuildConfig.API_KEY
    }

    @GET("v1/forecast.json")
    suspend fun getFullWeather(
        @Query("key") key: String = WEATHER_API,
        @Query("q") city: String,
        @Query("aqi") aqi: String = "yes",
        @Query("alerts") alerts: String = "yes",
        @Query("days") days: Int = 14,
    ): Response<WeatherModel>



}