package com.android.weatherui.common.domain.resources

sealed class NetworkResponseState<out T> {

    // Loading
    data object Loading: NetworkResponseState<Nothing>()
    // Failure
    data class Failure(val error: Throwable): NetworkResponseState<Nothing>()
    // Success
    data class Success<T>(val data: T): NetworkResponseState<T>()

}