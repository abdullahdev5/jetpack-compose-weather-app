package com.android.weatherui.common.data.module

import android.content.Context
import android.content.SharedPreferences
import android.location.LocationManager
import com.android.weatherui.common.data.api.WeatherApiService
import com.android.weatherui.weather.data.repository.WeatherRepositoryImpl
import com.android.weatherui.weather.domain.models.weather_related.WeatherModel
import com.android.weatherui.weather.domain.repository.WeatherRepository
import com.android.weatherui.weather.ui.viewmodels.CitySelectionViewModel
import com.android.weatherui.weather.ui.viewmodels.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit


private const val BASE_URL = "https://api.weatherapi.com/"



val appModule = module {

//    single<WeatherApiService> {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(
//                OkHttpClient
//                    .Builder()
//                    .readTimeout(30, TimeUnit.SECONDS)
//                    .connectTimeout(30, TimeUnit.SECONDS)
//                    .writeTimeout(30, TimeUnit.SECONDS)
//                    .build()
//            )
//            .build()
//            .create(WeatherApiService::class.java)
//    }

    single<WeatherApiService> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder().setLenient()
                    .serializeNulls()
                    .create()
            ))
            .client(
                OkHttpClient
                    .Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(WeatherApiService::class.java)
    }



    // Weather Repository
    singleOf(::WeatherRepositoryImpl) { bind<WeatherRepository>() }

    // Weather ViewModel
    viewModelOf(::WeatherViewModel)


    // City Selection ViewModel For Test
    viewModelOf(::CitySelectionViewModel)


    // Fused Location Provider Client
    single<FusedLocationProviderClient> {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }

    // LocationManager
    single<LocationManager> {
        androidContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }


    single<SharedPreferences>() {
        androidContext().getSharedPreferences("WeatherUIPrefs", Context.MODE_PRIVATE)
    }

}