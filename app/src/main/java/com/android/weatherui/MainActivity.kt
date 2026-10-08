package com.android.weatherui

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.Manifest
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.android.weatherui.ui.theme.WeatherUITheme
import com.android.weatherui.weather.ui.viewmodels.WeatherViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = Color.Transparent.toArgb(),
            ),
            navigationBarStyle = SystemBarStyle.dark(
                scrim = Color.Transparent.toArgb(),
            )
        )

//        if (!this.hasLocationPermission()) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                ),
//                1
//            )
//        }

        setContent {
            WeatherUITheme {

                val weatherViewModel = koinViewModel<WeatherViewModel>()

                Surface(modifier = Modifier.fillMaxSize()) {

                    val navHostController = rememberNavController()

                    NavigationGraph(
                        navHostController = navHostController,
                        weatherViewModel = weatherViewModel
                    )


                }
            }
        }
    }
}


fun Double.takeIntIfAfterDecimalZero(): String {
    val value = toDouble()
    val afterIndexValue = value.toString().substringAfter(".")
    if (afterIndexValue.startsWith("0")) {
        return toInt().toString()
    } else {
        return value.toString()
    }
}

suspend fun SnackbarHostState.showSnackBarRequest(
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Long,
    withDismissAction: Boolean,
    onActionPerformed: () -> Unit,
    onDismiss: () -> Unit,
) {


    dismissCurrentSnackBar()

    val snackBarResult = showSnackbar(
        message = message,
        actionLabel = actionLabel,
        duration = duration,
        withDismissAction = withDismissAction,
    )

    when (snackBarResult) {
        SnackbarResult.ActionPerformed -> {
            onActionPerformed()
        }

        SnackbarResult.Dismissed -> {
            onDismiss()
        }

        else -> {}
    }

}

fun SnackbarHostState.dismissCurrentSnackBar(): Unit {
    currentSnackbarData?.dismiss()
}
