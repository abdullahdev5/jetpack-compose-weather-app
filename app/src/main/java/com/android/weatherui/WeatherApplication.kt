package com.android.weatherui

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.util.DebugLogger
import com.android.weatherui.common.data.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class WeatherApplication: Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApplication)
            androidLogger(Level.DEBUG)
            // Modules
            modules(
                listOf(appModule)
            )
        }

    }


    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader(context)
            .newBuilder()
            .logger(DebugLogger())
            .build()
    }

}