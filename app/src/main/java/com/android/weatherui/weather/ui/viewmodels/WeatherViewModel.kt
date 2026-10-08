package com.android.weatherui.weather.ui.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weatherui.common.domain.constants.NetworkResponseStatus
import com.android.weatherui.common.domain.resources.NetworkResponseState
import com.android.weatherui.weather.domain.models.weather_related.WeatherAlerts
import com.android.weatherui.weather.domain.models.weather_related.WeatherForecast
import com.android.weatherui.weather.domain.models.weather_related.ForecastDay
import com.android.weatherui.weather.domain.models.weather_related.WeatherModel
import com.android.weatherui.weather.domain.repository.WeatherRepository
import com.android.weatherui.weather.domain.resources.LocationState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okio.IOException

private const val TAG = "WeatherViewModel"

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val sharedPreferences: SharedPreferences,
//    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _weatherModel = MutableStateFlow<WeatherModel?>(null)
    val weatherModel = _weatherModel.asStateFlow()


    private val _fullForecastDaysList = MutableStateFlow<List<ForecastDay>>(emptyList())
    val fullForecastDaysList = _fullForecastDaysList.asStateFlow()


    var fullForecastDaysListSize by mutableIntStateOf(0)
        private set


    var currentWeatherResponseStatus by mutableStateOf("")
        private set


    var currentUserLatLng by mutableStateOf<LatLng?>(null)
        private set


    var errorMessage by mutableStateOf("")


    var isRefreshing by mutableStateOf(false)
        private set


    val currentUserLatLngFromPref get() = getLastLocation()

    val isLocationEnabled get() = weatherRepository.isLocationEnabled



    fun getLastLocation(): LatLng? {
        val lat = sharedPreferences.getFloat("latitude", Float.MIN_VALUE)
        val lng = sharedPreferences.getFloat("longitude", Float.MIN_VALUE)
        return if (lat != Float.MIN_VALUE && lng != Float.MIN_VALUE) {
            LatLng(lat.toDouble(), lng.toDouble())
        } else {
            null
        }
    }

//    fun onStoreCityThatUserSelectFromSearch(city: String) {
//        savedStateHandle["city"] = city
//    }



    init {
        println("init Block")
//        getUserCurrentLocationLatLng()
//        getCurrentWeather(
//            city = CityData.Companion.Cities.Pakistan.Faisalabad,
//            days = 14
//        )
    }


    fun getCurrentWeather(city: String) {

//        if (_weatherModel.value != null) {
//            _weatherModel.value = null
//        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherRepository
                    .getCurrentWeatherWithForecast(
                        city = city,
                    ).collectLatest { result ->
                        when (result) {

                            NetworkResponseState.Loading -> {

                                isRefreshing = true

                                errorMessage = ""
                                currentWeatherResponseStatus = NetworkResponseStatus.LOADING
                            }

                            is NetworkResponseState.Failure -> {

                                Log.d(TAG, "getCurrentWeather: Failed: ${result.error.localizedMessage ?: ""}")

                                isRefreshing = false

                                currentWeatherResponseStatus = NetworkResponseStatus.FAILURE
                                errorMessage = result.error.localizedMessage ?: ""
                            }

                            is NetworkResponseState.Success -> {

                                isRefreshing = false

                                /* Data */
//                                _weatherModel.value = result.data

                                result.data?.let { data ->
                                    _weatherModel.value = WeatherModel(
                                        current = data.current,
                                        location = data.location,
                                        forecast = WeatherForecast(
                                            forecastDay = (if ((data.forecast.forecastDay.size) > 5)
                                                data.forecast.forecastDay.take(5)
                                            else data.forecast.forecastDay)
                                        ),
                                        alerts = WeatherAlerts(
                                            data.alerts.alert
                                        )
                                    )

                                    _fullForecastDaysList.value = data.forecast.forecastDay

                                    fullForecastDaysListSize = _fullForecastDaysList.value.size

                                    currentWeatherResponseStatus = NetworkResponseStatus.SUCCESS
                                    errorMessage = ""

                                } ?: {
                                    currentWeatherResponseStatus = NetworkResponseStatus.FAILURE
                                    errorMessage = "Unknown Error! Failed to fetch the Weather Information"
                                }

                                Log.d(TAG, "getCurrentWeather: Success: Location: ${_weatherModel.value?.location}")
                                Log.d(TAG, "getCurrentWeather: Success: Current Weather: ${_weatherModel.value?.current}")


                            }
                        }
                    }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
                isRefreshing = false
                errorMessage = "Unable to Refresh, No Internet Connection!"

            } catch (exception: Exception) {
                exception.printStackTrace()
                isRefreshing = false
                errorMessage = exception.message ?: ""
                Log.d(TAG, "getCurrentWeather: Exception: $errorMessage")
            }

        }
    }

    fun getUserCurrentLocationLatLng(
        context: Context,
        onLocationEnabledOrNot: ((LatLng?) -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {

                println("Location: getUserCurrentLocationLatLng()")

                weatherRepository
                    .getUserCurrentLocation(
                        context = context,
                    )
                    .collectLatest { result ->

                        when(result) {

                            is LocationState.Loading -> {
                                isRefreshing = true
                            }

                            is LocationState.Error -> {
                                errorMessage = result.error.message ?: ""
                                isRefreshing = false
                            }

                            is LocationState.LocationEnabled -> {

                                val data = result.data

                                isRefreshing = false

                                onLocationEnabledOrNot?.let {
                                    onLocationEnabledOrNot.invoke(data)
                                }

                                data?.let {

                                    currentUserLatLng = data

                                    with(sharedPreferences.edit()) {
                                        putFloat(
                                            "latitude",
                                            data.latitude.toFloat()
                                        )
                                        putFloat(
                                            "longitude",
                                            data.longitude.toFloat()
                                        )
                                        apply()
                                    }

//                                    onLocationEnabled(data)

                                    errorMessage = ""

                                } ?: run {
                                    errorMessage = "Unknown Error"
                                }

                            }
//                            is LocationState.LocationNotEnabled -> {
//                                println("Location: getUserCurrentLocationLatLng(), Location Not Enabled")
//                                onLocationNotEnabled!!(result.intentSenderRequest)
//                                return@collectLatest
//                            }
                            else -> {}
                        }

                    }

            } catch (ioException: IOException) {
                ioException.printStackTrace()
                isRefreshing = false
                errorMessage = "Unable to Refresh, No Internet Connection!"

            } catch (exception: Exception) {
                exception.printStackTrace()
                isRefreshing = false
                errorMessage = exception.localizedMessage ?: ""
            }
        }
    }

    fun checkLocationSetting(
        context: Context,
        onLocationEnabled: (() -> Unit)? = null,
        onLocationDisabled: (IntentSenderRequest?) -> Unit,
    ) = weatherRepository.requestLocationSetting(
        context = context,
        onLocationEnabled = onLocationEnabled,
        onLocationDisabled = onLocationDisabled
    )


}