package com.android.weatherui.weather.domain.repository

import android.content.Context
import androidx.activity.result.IntentSenderRequest
import com.android.weatherui.common.domain.resources.NetworkResponseState
import com.android.weatherui.weather.domain.models.weather_related.WeatherModel
import com.android.weatherui.weather.domain.resources.LocationState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {


    val isLocationEnabled: Boolean


    suspend fun getCurrentWeatherWithForecast(
        city: String,
    ): Flow<NetworkResponseState<WeatherModel?>>

    fun getUserCurrentLocation(
        context: Context,
    ): Flow<LocationState<LatLng?>>

    fun requestLocationSetting(
        context: Context,
        onLocationEnabled: (() -> Unit)?,
        onLocationDisabled: (IntentSenderRequest?) -> Unit,
//        onException: (Exception) -> Unit,
    )

}