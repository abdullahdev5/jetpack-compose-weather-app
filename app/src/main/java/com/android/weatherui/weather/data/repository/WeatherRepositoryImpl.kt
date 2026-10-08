package com.android.weatherui.weather.data.repository

import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.android.weatherui.common.data.api.WeatherApiService
import com.android.weatherui.common.domain.resources.NetworkResponseState
import com.android.weatherui.weather.domain.models.weather_related.WeatherModel
import com.android.weatherui.weather.domain.repository.WeatherRepository
import com.android.weatherui.weather.domain.resources.LocationState
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Error


private const val TAG = "WeatherRepositoryImpl"

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApiService,
    private val locationClient: FusedLocationProviderClient,
    private val locationManager: LocationManager,
//    private val context: Context,
) : WeatherRepository {


    override val isLocationEnabled: Boolean
        get() = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


    override suspend fun getCurrentWeatherWithForecast(
        city: String,
    ): Flow<NetworkResponseState<WeatherModel?>> {
        return callbackFlow {

            trySend(NetworkResponseState.Loading)

            val response = weatherApi.getFullWeather(city = city)

            println("Response: $response")

            try {

                if (response.isSuccessful) {

                    println("Response: Success")

                    val data = response.body()

                    trySend(NetworkResponseState.Success(data))

                } else {
                    trySend(NetworkResponseState.Failure(error(response.code().toString())))
                    Log.e(
                        TAG,
                        "getCurrentWeatherWithForecast: Un Successful: ${response.code()}",
                    )
                }

            } catch (exception: Exception) {
                println("Response: Failure")

                println("Response: Failure: $exception")


                trySend(NetworkResponseState.Failure(exception))
            }

            awaitClose {
                close()
            }
        }
    }

    @Suppress("MissingPermission")
    override fun getUserCurrentLocation(
        context: Context,
    ): Flow<LocationState<LatLng?>> {
        return callbackFlow {

//            if (!context.hasLocationPermission()) {
//                trySend(LocationState.Error(kotlin.Error("Permission Denied!")))
//            }

//            val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            trySend(LocationState.Loading)


            if (isLocationEnabled) {

                val priority = Priority.PRIORITY_HIGH_ACCURACY


                locationClient.getCurrentLocation(
                    priority,
                    CancellationTokenSource().token
                ).addOnSuccessListener { result: Location? ->

                    result?.let {
                        val latitude = result.latitude
                        val longitude = result.longitude

                        val latLng = LatLng(latitude, longitude)

                        println("Location: Success")
                        println("Location: LatLng: $latLng")

                        trySend(LocationState.LocationEnabled(latLng))

                    } ?: run {
                        println("Location: Result is Null")
                        trySend(LocationState.Error(Error("Unable to fetch Your Current City's Location Weather")))
                    }


                }.addOnFailureListener { error ->
                    println("Location: Failed")
                    trySend(LocationState.Error(Error("Unable to fetch Your Current City's Location Weather")))
                }


            } else {
                trySend(LocationState.LocationDisabled)
            }

//                val priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY

//            val request = LocationRequest.Builder(10000L)
//                .setIntervalMillis(10000L)
//                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
//                .build()
//
//            val locationCallback = object : LocationCallback() {
//                override fun onLocationResult(locationResult: LocationResult) {
//                    locationResult.locations.lastOrNull()?.let {
//                        trySend(LatLng(it.latitude, it.longitude))
//                    }
//                }
//            }
//
//            locationClient.requestLocationUpdates(
//                request,
//                locationCallback,
//                Looper.getMainLooper()
//            )

            awaitClose {
//                locationClient.removeLocationUpdates(locationCallback)
                close()
            }

        }
    }


    override fun requestLocationSetting(
        context: Context,
        onLocationEnabled: (() -> Unit)?,
        onLocationDisabled: (IntentSenderRequest?) -> Unit,
//        onException: (Exception) -> Unit,
    ) {

//        val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


//        if (!isLocationEnabled) {

        val locationRequest = LocationRequest.Builder(1000L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setWaitForAccurateLocation(false)
            .setGranularity(Granularity.GRANULARITY_FINE)
            .setMinUpdateIntervalMillis(1000L)
            .setMaxUpdateDelayMillis(2000L)
            .build()

        val client: SettingsClient = LocationServices.getSettingsClient(context)

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)


        val gpsSettingsTask: Task<LocationSettingsResponse?> =
            client.checkLocationSettings(builder.build())


        gpsSettingsTask.addOnSuccessListener {

            onLocationEnabled?.let {
                onLocationEnabled()
            }

        }.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {

                try {

                    val intentSenderRequest = IntentSenderRequest
                        .Builder(exception.resolution)
                        .build()

                    onLocationDisabled(intentSenderRequest)

                } catch (sendException: IntentSender.SendIntentException) {
//                    onException(sendException)
                    println("Location: Send Intent Request Exception ${sendException.message}")
                }

            } else {
                exception.printStackTrace()
//                onException(exception)
                println("Location: Location Not Enabled Exception ${exception.message}")
            }
        }

//        } else {
//
//        }

    }


}