package com.android.weatherui.weather.domain.resources

import androidx.activity.result.IntentSenderRequest

sealed class LocationState<out T> {

    data object Loading: LocationState<Nothing>()

    data class LocationEnabled<T>(val data: T): LocationState<T>()

    data object LocationDisabled: LocationState<Nothing>()

    data class Error(val error: Throwable): LocationState<Nothing>()


}