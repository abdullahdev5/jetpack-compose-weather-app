package com.android.weatherui.weather.ui.screens

import android.Manifest
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ExperimentalComposeRuntimeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.android.weatherui.R
import com.android.weatherui.common.domain.extensions.goToLocationSettings
import com.android.weatherui.common.domain.extensions.goToPermissionSettings
import com.android.weatherui.common.ui.ProgressIndicator
import com.android.weatherui.dismissCurrentSnackBar
import com.android.weatherui.common.domain.extensions.getFullUrlOfTempConditionIconFromApi
import com.android.weatherui.common.domain.extensions.hasLocationPermission
import com.android.weatherui.common.domain.extensions.isLocationEnabled
import com.android.weatherui.showSnackBarRequest
import com.android.weatherui.common.domain.extensions.takeFirstThreeWithFirstUpper
import com.android.weatherui.takeIntIfAfterDecimalZero
import com.android.weatherui.ui.theme.Black
import com.android.weatherui.ui.theme.Blue
import com.android.weatherui.ui.theme.Brown
import com.android.weatherui.ui.theme.BrownDay
import com.android.weatherui.ui.theme.DarkBackgroundColor
import com.android.weatherui.ui.theme.DarkBlue
import com.android.weatherui.ui.theme.DarkBrownDay
import com.android.weatherui.ui.theme.DarkCardBackgroundColor
import com.android.weatherui.ui.theme.Gray
import com.android.weatherui.ui.theme.Green
import com.android.weatherui.ui.theme.LightBlue
import com.android.weatherui.ui.theme.Orange
import com.android.weatherui.ui.theme.Purple
import com.android.weatherui.ui.theme.Red
import com.android.weatherui.ui.theme.WeatherUITheme
import com.android.weatherui.ui.theme.White
import com.android.weatherui.ui.theme.Yellow
import com.android.weatherui.weather.domain.constants.AirQualityRelated
import com.android.weatherui.weather.domain.constants.InlineTextContentIds
import com.android.weatherui.weather.domain.constants.MoonPhase
import com.android.weatherui.weather.domain.constants.WeatherRelated
import com.android.weatherui.weather.domain.models.weather_related.CurrentWeather
import com.android.weatherui.weather.domain.models.weather_related.ForecastDay
import com.android.weatherui.weather.domain.models.weather_related.ForecastHour
import com.android.weatherui.weather.domain.models.weather_related.WeatherAirQuality
import com.android.weatherui.weather.domain.models.weather_related.WeatherAstronomy
import com.android.weatherui.weather.domain.models.weather_related.WeatherLocation
import com.android.weatherui.weather.domain.models.weather_related.isCurrentForecastHour
import com.android.weatherui.weather.ui.common_composables.FancyCard
import com.android.weatherui.weather.ui.common_composables.InlineTextContentIcon
import com.android.weatherui.weather.ui.common_composables.ShimmerLoadingEffect
import com.android.weatherui.weather.ui.common_composables.SimpleCard
import com.android.weatherui.weather.ui.viewmodels.WeatherViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.text.appendLine

private const val TAG = "HomeScreen.kt"

private const val maxDragToRefreshFloat = 150f


@OptIn(
    ExperimentalComposeRuntimeApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    currentWeather: CurrentWeather?,
    currentWeatherLocation: WeatherLocation?,
    currentAirQuality: WeatherAirQuality?,
    forecastDaysList: List<ForecastDay>,
    currentDayForecast: ForecastDay?,
    weatherViewModel: WeatherViewModel,
    onHourlyForecastMoreClick: (
        currentFormattedTimeString: String,
        isGoingForNextTwoHours: Boolean,
    ) -> Unit,
    onDailyForecastMoreClick: () -> Unit,
    onDetailsClick: () -> Unit,
    onAirQualityMoreClick: (airQualityLevel: Int) -> Unit,
    onCitySelectionClick: () -> Unit,
    isUserSelectCityFromSearch: () -> Boolean,
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current


    var isLocationEnabled by rememberSaveable() {
        mutableStateOf(weatherViewModel.isLocationEnabled)
    }

    var isCustomLocationPermissionDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }

    var isLocationPermissionGranted by rememberSaveable {
        mutableStateOf(context.hasLocationPermission())
    }

    var hasResumeOnce by rememberSaveable {
        mutableStateOf(false)
    }


    // For Showing the Map After Few Minutes
    var currentWeatherLocationNotNullAfterDebounce by rememberSaveable() {
        mutableStateOf<Boolean?>(null)
    }

    val currentUserLocationLatLng by remember() {
        derivedStateOf {
            weatherViewModel.currentUserLatLng
        }
    }

    val currentUserLocationLatLngFromPref by remember() {
        derivedStateOf {
            weatherViewModel.currentUserLatLngFromPref
        }
    }

    var currentLifecycleEvent by rememberSaveable {
        mutableStateOf<Lifecycle.Event?>(null)
    }


    val permissionState = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { it: Map<String, @JvmSuppressWildcards Boolean> ->

            val misLocationPermissionGranted = it.all { it.value }

            isLocationPermissionGranted = misLocationPermissionGranted

            if (!misLocationPermissionGranted) {
                isCustomLocationPermissionDialogOpen = true
            } else {
                isCustomLocationPermissionDialogOpen = false
            }

            Log.d(
                TAG,
                "HomeScreen: permission State: Location Permission: $isLocationPermissionGranted"
            )

        }
    )

    LaunchedEffect(Unit) {
        if (currentWeather == null && !isUserSelectCityFromSearch()) {

            permissionState.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )

        }
    }


    LaunchedEffect(
        key1 = Unit,
        key2 = isLocationPermissionGranted,
        key3 = isLocationEnabled,
    ) {


        if (currentWeather == null && !isUserSelectCityFromSearch()) {

            Log.d(
                TAG,
                "HomeScreen: Launched Effect: Location Permission: $isLocationPermissionGranted"
            )

            if (isLocationEnabled) {

                Log.d(
                    TAG,
                    "HomeScreen: LaunchedEffect for onCreate: Location is Enabled: $isLocationEnabled"
                )

                weatherViewModel.getUserCurrentLocationLatLng(
                    context = context,
                    onLocationEnabledOrNot = { latLng ->
                        latLng?.let { latLng ->
                            weatherViewModel.getCurrentWeather(
                                city = "${latLng.latitude},${latLng.longitude}",
                            )
                        }
                    }
                )
            } else {

                Log.d(
                    TAG,
                    "HomeScreen: LaunchedEffect for onCreate: Location is Disabled: $isLocationEnabled"
                )

                currentUserLocationLatLngFromPref?.let { latLng ->

                    Log.d(
                        TAG,
                        "HomeScreen: LaunchedEffect for onCreate: User Lat Lng From prefs in Not Null: $currentUserLocationLatLngFromPref"
                    )

                    weatherViewModel.getCurrentWeather("${latLng.latitude},${latLng.longitude}")
                }


                // Requesting
                if (!isLocationEnabled) {

                    Log.d(
                        TAG,
                        "HomeScreen: LaunchedEffect for onCreate: Location is Not Enabled: $isLocationEnabled"
                    )

                    scope.launch(Dispatchers.Default) {

                        snackBarHostState.showSnackBarRequest(
                            message = context.getString(R.string.location_request_error_msg),
                            actionLabel = "Settings",
                            duration = SnackbarDuration.Indefinite,
                            withDismissAction = true,
                            onDismiss = {
                                snackBarHostState.dismissCurrentSnackBar()
                            },
                            onActionPerformed = {
                                context.goToLocationSettings()
                            }
                        )

                    }

                } else {

                    Log.d(
                        TAG,
                        "HomeScreen: LaunchedEffect for onCreate: Both Are Enabled: Permission: $isLocationPermissionGranted, Device Location: $isLocationEnabled"
                    )

                    weatherViewModel.getUserCurrentLocationLatLng(
                        context = context,
                        onLocationEnabledOrNot = { latLng ->
                            latLng?.let { latLng ->
                                weatherViewModel.getCurrentWeather(
                                    city = "${latLng.latitude},${latLng.longitude}",
                                )
                            }
                        }
                    )
                }


            }


        }


    }




    DisposableEffect(
        key1 = lifecycleOwner.lifecycle,
        key2 = currentLifecycleEvent,
    ) {

        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {

                    currentLifecycleEvent = Lifecycle.Event.ON_CREATE


                }

                Lifecycle.Event.ON_RESUME -> {

                    if (!hasResumeOnce) {
                        hasResumeOnce = true
                        return@LifecycleEventObserver
                    }

                    isLocationPermissionGranted = context.hasLocationPermission()
                    isLocationEnabled = context.isLocationEnabled()

                    currentLifecycleEvent = Lifecycle.Event.ON_RESUME


                    if (currentWeather == null && !isUserSelectCityFromSearch()) {

                        currentLifecycleEvent?.let {

                            Log.d(
                                TAG,
                                "HomeScreen: LaunchedEffect for onResume: CurrentLifecycleEvent is Not Null: $currentLifecycleEvent"
                            )

                            if (currentLifecycleEvent == Lifecycle.Event.ON_RESUME) {


                                // Requesting
                                if (!isLocationEnabled) {

                                    Log.d(
                                        TAG,
                                        "HomeScreen: LaunchedEffect for onResume: Device Location Disabled: $isLocationPermissionGranted"
                                    )

                                    scope.launch(Dispatchers.Default) {

                                        snackBarHostState.showSnackBarRequest(
                                            message = context.getString(R.string.location_request_error_msg),
                                            actionLabel = "Settings",
                                            duration = SnackbarDuration.Indefinite,
                                            withDismissAction = true,
                                            onDismiss = {
                                                snackBarHostState.dismissCurrentSnackBar()
                                            },
                                            onActionPerformed = {
                                                context.goToLocationSettings()
                                            }
                                        )

                                    }

                                } else {

                                    Log.d(
                                        TAG,
                                        "HomeScreen: LaunchedEffect for onCreate: Both Are Enabled: Permission: $isLocationPermissionGranted, Device Location: $isLocationEnabled"
                                    )

                                    weatherViewModel.getUserCurrentLocationLatLng(
                                        context = context,
                                        onLocationEnabledOrNot = { latLng ->
                                            latLng?.let { latLng ->
                                                weatherViewModel.getCurrentWeather(
                                                    city = "${latLng.latitude},${latLng.longitude}",
                                                )
                                            }
                                        }
                                    )
                                }


                            }


                        }

                    }


                }

                else -> {}
            }
        }


        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)


        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }


    LaunchedEffect(currentWeatherLocation) {
        currentWeatherLocation?.let {
            snapshotFlow { currentWeatherLocation }
                .debounce(3000)
                .collectLatest { result ->
                    currentWeatherLocationNotNullAfterDebounce = result != null
                }
        }
    }

    val currentTimeDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    val localDateTime by remember(currentWeatherLocation, currentWeatherLocation) {
        derivedStateOf {
            currentWeatherLocation?.let {
                try {
                    LocalDateTime.parse(
                        it.localtime,
                        currentTimeDateFormatter
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    LocalDateTime.now()
                }
            } ?: LocalDateTime.now()
        }
    }


    val currentWeekName by remember(localDateTime) {
        mutableStateOf(localDateTime.dayOfWeek.name.takeFirstThreeWithFirstUpper())
    }


    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())

    val currentCityFormattedTime by remember(localDateTime, currentWeather) {
        derivedStateOf {
            try {
                localDateTime!!.format(timeFormatter)
            } catch (e: Exception) {
                e.printStackTrace()
                "Err"
            }
        }
    }


    val isDay by remember(currentWeather) {
        derivedStateOf {
            (currentWeather?.isDay ?: 0) == WeatherRelated.IS_DAY
        }
    }

    val animatedRefreshOffsetY = remember {
        Animatable(0f)
    }

    var isReleasedToRefresh by remember() {
        mutableStateOf(false)
    }

    var refreshCounts by rememberSaveable {
        mutableIntStateOf(0)
    }

    var refreshProgress by remember() {
        mutableFloatStateOf(0f)
    }


    // NestedScroll dispatcher and connection to handle custom drag events
    val nestedScrollDispatcher = remember { NestedScrollDispatcher() }
    val nestedScrollConnection = remember() {
        object : NestedScrollConnection {

            // For Refresh Progress
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                if (available.y < 0 && animatedRefreshOffsetY.value > 0) {
                    val delta = available.y / 2
                    val newOffset =
                        (animatedRefreshOffsetY.value + delta).coerceIn(0f, maxDragToRefreshFloat)

                    // Refresh Progress
                    refreshProgress = (newOffset / maxDragToRefreshFloat).coerceIn(0f, 1f)

                    scope.launch(Dispatchers.Default) {
                        animatedRefreshOffsetY.snapTo(newOffset)
                    }
                }

                return super.onPreScroll(available, source)
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {

                if (scrollState.value == 0 && available.y > 0f) {

                    val delta = available.y / 2
                    val newOffset =
                        (animatedRefreshOffsetY.value + delta).coerceIn(0f, maxDragToRefreshFloat)

                    refreshProgress = (newOffset / maxDragToRefreshFloat).coerceIn(0f, 1f)

                    scope.launch(Dispatchers.Default) {
                        animatedRefreshOffsetY.snapTo(newOffset)
                    }

                }

                return super.onPostScroll(consumed, available, source)
            }

            override suspend fun onPostFling(
                consumed: Velocity,
                available: Velocity,
            ): Velocity {
//
                if (refreshProgress >= 1f) {
                    isReleasedToRefresh = true
                    refreshProgress = 0f
                } else {
                    refreshProgress = 0f
                }


                scope.launch(Dispatchers.Default) {
                    animatedRefreshOffsetY.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(500)
                    )
                }
                return super.onPostFling(consumed, available)
            }

        }
    }



    LaunchedEffect(key1 = isReleasedToRefresh) {
        if (isReleasedToRefresh) {
            if (refreshCounts < 5) {

                if (currentWeatherLocation != null) {

                    weatherViewModel.getCurrentWeather(
                        city = "${currentWeatherLocation.latitude},${currentWeatherLocation.longitude}"
                    )

                    Log.d(TAG, "Location: Refreshing: Data is Not Null")
                } else {

                    if (!isLocationPermissionGranted) {

                        Log.d(TAG, "HomeScreen: Snack Bar 1")

                        isCustomLocationPermissionDialogOpen = true

                    } else if (!isLocationEnabled) {

                        scope.launch(Dispatchers.Default) {
                            snackBarHostState.showSnackBarRequest(
                                message = context.getString(R.string.location_request_error_msg),
                                actionLabel = "Settings",
                                duration = SnackbarDuration.Indefinite,
                                withDismissAction = true,
                                onDismiss = {
                                    snackBarHostState.dismissCurrentSnackBar()
                                },
                                onActionPerformed = {
                                    context.goToLocationSettings()
                                }
                            )
                        }

                    } else {
                        weatherViewModel.getUserCurrentLocationLatLng(
                            context = context,
                            onLocationEnabledOrNot = { latLng ->
                                latLng?.let { latLng ->
                                    weatherViewModel.getCurrentWeather(
                                        city = "${latLng.latitude},${latLng.longitude}",
                                    )
                                }
                            }
                        )

                    }


                }


            }

            delay(1000)
            isReleasedToRefresh = false
            refreshCounts++

            return@LaunchedEffect

        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            ) {
                Snackbar(
                    snackbarData = it,
                    containerColor = White,
                    contentColor = Black,
                    actionColor = Black,
                    actionContentColor = Black,
                    dismissActionContentColor = Black
                )
            }
        },
        topBar = {

            Column {

                val isScrollReachedToChangeTopBarColor by remember {
                    derivedStateOf {
                        scrollState.value > 50
                    }
                }

                val animatedTopBarColor by animateColorAsState(
                    targetValue = if (currentWeather == null) DarkCardBackgroundColor else {
                        if (isScrollReachedToChangeTopBarColor) {
                            if (isDay) {
                                BrownDay.copy(0.9f)
                            } else {
                                DarkBlue.copy(0.9f)
                            }
                        } else Color.Transparent
                    },
                    animationSpec = tween(1000),
                    label = ""
                )

                // Top Bar
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawRect(color = animatedTopBarColor)
                        }
                        .padding(all = 10.dp)
                        .statusBarsPadding()
                ) {
                    // Menu Drawer Icon
                    Icon(
                        painter = painterResource(R.drawable.menu_icon),
                        contentDescription = "Icon for Menu Drawer",
                        tint = White,
                        modifier = modifier
                            .align(Alignment.CenterStart)
                    )

                    // City, Date & Time
                    Column(
                        modifier = modifier
                            .align(Alignment.Center)
                            .clickable {
                                onCitySelectionClick.invoke()
                            }
                    ) {
                        // City
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = currentWeatherLocation?.city ?: "City",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = White
                                )
                            )

                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "City Selection Icon",
                                tint = White
                            )
                        }

                        // Date & Time
                        Text(
                            text = "$currentWeekName $currentCityFormattedTime",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = White
                            )
                        )

                    }


                    // Actions Icon
                    Icon(
                        painter = painterResource(R.drawable.radar_icon),
                        contentDescription = null,
                        tint = White,
                        modifier = modifier
                            .align(Alignment.CenterEnd)
                    )

                }

                AnimatedVisibility(weatherViewModel.errorMessage.isNotEmpty()) {
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .background(color = Red)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier
//                                .padding(all = 5.dp)
                        ) {
                            Text(
                                text = weatherViewModel.errorMessage,
                                style = TextStyle(
                                    color = White,
                                    fontStyle = FontStyle.Italic
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = modifier
                                    .weight(1f)
                            )

                            IconButton(
                                onClick = {
                                    weatherViewModel.errorMessage = ""
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove Error Icon",
                                    tint = White,
                                )
                            }

                        }
                    }
                }

            }
        },
        modifier = modifier
            .fillMaxSize()

    ) { innerPadding ->


        val animatedScreenBackgroundColor by animateColorAsState(
            targetValue = if (currentWeather != null) {
                if (isDay) BrownDay else Blue
            } else DarkBackgroundColor,
            animationSpec = tween(200),
            label = "",
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(
                    bottom = innerPadding.calculateBottomPadding(),
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Rtl)
                )
                .drawBehind {
                    drawRect(color = animatedScreenBackgroundColor)
                }
        ) {

            Log.d(TAG, "HomeScreen: Current Lifecycle Event: $currentLifecycleEvent")

            AnimatedVisibility(
                visible = refreshProgress.toFloat() != 0f ||
                        isReleasedToRefresh ||
                        weatherViewModel.isRefreshing
            ) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(paddingValues = innerPadding)
                        .background(color = Color.Transparent)
                ) {

                    if (refreshProgress.toFloat() != 0f && !isReleasedToRefresh) {
                        ProgressIndicator(
                            progress = { refreshProgress },
                            color = Gray,
                            strokeWidth = 2.dp,
                            trackColor = White,
                            modifier = modifier
                                .padding(all = 10.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    } else {
                        ProgressIndicator(
                            modifier = modifier
                                .padding(all = 10.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    AnimatedVisibility(refreshProgress.toFloat() >= 1f) {
                        Text(
                            text = "Release to Refresh",
                            style = TextStyle(
                                color = White,
                                fontSize = 15.sp,
                            )
                        )
                    }

                }

            }


            Column(
                modifier = Modifier
                    .nestedScroll(
                        nestedScrollConnection,
                        nestedScrollDispatcher
                    )
                    .verticalScroll(state = scrollState)
                    .offset {
                        IntOffset(x = 0, y = animatedRefreshOffsetY.value.roundToInt())
                    }
            ) {

                // Top Container with Image and Feels Like
                TopSectionMain(
                    modifier = modifier,
                    innerPadding = innerPadding,
                    context = context,
                    currentWeather = currentWeather,
                    currentDayForecast = currentDayForecast,
                    isDay = { isDay },
                    onNextTwoHoursClick = {
                        onHourlyForecastMoreClick.invoke(
                            currentCityFormattedTime,
                            true,
                        )
                    },
                )

                // When All Data is Not Null Then We are Showing the Data
                if (
                    currentWeather != null && currentWeatherLocation != null
                    && forecastDaysList.isNotEmpty() && currentDayForecast != null
                ) {


                    val animatedCardsColor by animateColorAsState(
                        targetValue = if (isDay) DarkBrownDay else DarkBlue,
                        animationSpec = tween(500),
                        label = ""
                    )


                    // Hourly Forecast
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = animatedCardsColor
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(all = 10.dp)
                            .clickable {

                                onHourlyForecastMoreClick.invoke(
                                    currentCityFormattedTime,
                                    false,
                                )

                            }
                    ) {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Hourly Forecast",
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = modifier
                                        .padding(top = 5.dp, start = 20.dp)
                                )
                                Spacer(modifier = modifier.weight(1f))
                                // Hourly Forecast Fancy Card
                                FancyCard(
                                    text = "72 Hours",
                                    isDay = {
                                        isDay
                                    },
                                    modifier = modifier
                                )

                            }
                        }

//                val forecastHourList = weatherModel?.forecast?.forecastday?.flatMap { it.hour ?: emptyList() } ?: emptyList()

//                        val forecastHourList by remember(
//                            forecastDaysList,
//                            currentDayForecast,
//                            currentWeather
//                        ) {
//                            derivedStateOf {
//                                currentDayForecast.hour
//                            }
//                        }

                        val forecastHourList by remember(
                            forecastDaysList, currentDayForecast, currentWeather
                        ) {
                            mutableStateOf(currentDayForecast.hour)
                        }

                        AnimatedVisibility(
                            visible = forecastHourList.isNotEmpty(),
                            enter = fadeIn() + slideInHorizontally(
                                initialOffsetX = { -it }
                            ),
                            exit = fadeOut() + slideOutHorizontally(
                                targetOffsetX = { -it }
                            )
                        ) {

                            // List
                            LazyRow(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {

                                items(
                                    count = forecastHourList.size,
                                    key = {
                                        it
                                    }
                                ) { index ->

                                    val forecastHour = forecastHourList[index]

                                    HourlyForecastListItemMain(
                                        context = context,
                                        forecastHour = forecastHour,
                                        currentCityFormattedTime = currentCityFormattedTime,
                                    )
                                }

                            }

                        }

                        if (forecastHourList.isEmpty()) {
                            ProgressIndicator(
                                modifier = modifier
                                    .padding(all = 10.dp)
                            )
                        }

                    }

                    // Daily Forecast
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = animatedCardsColor
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(all = 10.dp)
                            .clickable {
                                onDailyForecastMoreClick.invoke()
                            }
                    ) {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Daily Forecast",
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = modifier
                                        .padding(top = 5.dp, start = 20.dp)
                                )
                                Spacer(modifier = modifier.weight(1f))

                                val fullForecastDaysListSize by remember() {
                                    derivedStateOf {
                                        weatherViewModel.fullForecastDaysListSize
                                    }
                                }

                                // Daily Forecast Fancy Card
                                FancyCard(
                                    text = "$fullForecastDaysListSize days",
                                    isDay = {
                                        isDay
                                    },
                                    modifier = modifier
                                )

                            }
                        }

                        AnimatedVisibility(
                            visible = forecastDaysList.isNotEmpty(),
                            enter = fadeIn() + slideInVertically(
                                initialOffsetY = { -it }
                            ),
                            exit = fadeOut() + slideOutVertically(
                                targetOffsetY = { -it }
                            )
                        ) {
                            Column {
                                forecastDaysList.forEach { forecastDay ->
                                    DailyForecastListItemMain(
                                        context = context,
                                        modifier = modifier,
                                        forecastDay = forecastDay,
                                        currentLocalDateTime = localDateTime,
                                    )
                                }
                            }
                        }

                        if (forecastDaysList.isEmpty()) {
                            ProgressIndicator(
                                modifier = modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(all = 10.dp)
                            )
                        }


                    }

                    // Details
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = animatedCardsColor
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(all = 10.dp)
                            .clickable {
                                onDetailsClick.invoke()
                            }
                    ) {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Details",
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = modifier
                                        .padding(top = 5.dp, start = 20.dp)
                                )
                                Spacer(modifier = modifier.weight(1f))
                                // Details Fancy Card
                                FancyCard(
                                    text = "More",
                                    isDay = {
                                        isDay
                                    },
                                    modifier = modifier
                                )

                            }
                        }

                        DetailsUIMain(
                            modifier = modifier,
                            context = context,
                            currentWeather = currentWeather
                        )
                    }

                    // Wind & Pressure
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = animatedCardsColor
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(all = 10.dp)
                    ) {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Wind & Pressure",
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = modifier
                                        .padding(top = 5.dp, start = 20.dp)
                                )
                                Spacer(modifier = modifier.weight(1f))
                                // Wind & Pressure Fancy Card
//                            FancyCard(
//                                text = "More",
//                                isDay = {
//                                    isDay
//                                },
//                                modifier = modifier
//                            )

                            }
                        }

                        WindAndPressureUIMain(
                            modifier = modifier,
                            currentWeather = currentWeather
                        )
                    }


                    // Google Map & User Current Location
                    AnimatedVisibility(
                        currentWeatherLocationNotNullAfterDebounce != null
                    ) {

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = animatedCardsColor
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(all = 10.dp)
                        ) {
                            Column(
                                modifier = modifier
                                    .fillMaxWidth()
                            ) {

                                var isLocationCoordinateShifted by rememberSaveable() {
                                    mutableStateOf(false)
                                }

                                Row(
                                    modifier = modifier
                                        .fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                                        modifier = modifier
                                            .padding(top = 5.dp, start = 20.dp)
                                    ) {
                                        AnimatedContent(
                                            targetState = !isLocationCoordinateShifted &&
                                                    (currentUserLocationLatLng != null ||
                                                            currentUserLocationLatLngFromPref != null),
                                            label = ""
                                        ) { targetState ->

                                            val testToShow by remember {
                                                derivedStateOf {
                                                    if (targetState) "Your Last Location"
                                                    else "Selected City's Location"
                                                }
                                            }

                                            Text(
                                                text = testToShow,
                                                color = White,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = "Location Icon",
                                            tint = White,
                                            modifier = modifier
                                                .width(15.dp)
                                                .height(15.dp)
                                        )
                                    }
                                    Spacer(modifier = modifier.weight(1f))
                                    // Current Coordinates Shift Icon
                                    if (currentUserLocationLatLng != null || currentUserLocationLatLngFromPref != null) {
                                        Image(
                                            painter = painterResource(R.drawable.arrow_shift_icon),
                                            contentDescription = "Icon for Shift the Current User Map LatLng to Selected City LatLng",
                                            modifier = modifier
                                                .padding(end = 10.dp, top = 5.dp)
                                                .clickable {
                                                    isLocationCoordinateShifted =
                                                        !isLocationCoordinateShifted
                                                }
                                        )
                                    }

                                }

                                // If Current User LatLng Null then Providing the Current City LatLng

                                val currentCityLocationLatLng by remember(currentWeatherLocation) {
                                    derivedStateOf {
                                        LatLng(
                                            currentWeatherLocation.latitude,
                                            currentWeatherLocation.longitude
                                        )
                                    }
                                }

                                val mLatLng by remember(
                                    currentUserLocationLatLng,
                                    currentWeatherLocation
                                ) {
                                    derivedStateOf {
                                        if (currentUserLocationLatLng != null)
                                            currentUserLocationLatLng
                                        else if (currentUserLocationLatLngFromPref != null)
                                            currentUserLocationLatLngFromPref
                                        else currentCityLocationLatLng
                                    }
                                }

//                    LatLng(31.4167, 73.0833) // Faisalabad LatLng

                                GoogleMapsUIMain(
                                    modifier = modifier,
                                    mLatLng = { mLatLng!! },
                                    isLocationCoordinatesShifted = isLocationCoordinateShifted,
                                    currentCityLocationLatLng = { currentCityLocationLatLng },
                                )


                            }


                        }

                    }

                    // Hurricane Moon Phase
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = animatedCardsColor
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(all = 10.dp)
                    ) {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Hurricane",
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = modifier
                                        .padding(top = 5.dp, start = 20.dp)
                                )
                                Spacer(modifier = modifier.weight(1f))
                                // Hurricane Fancy Card
//                            FancyCard(
//                                text = "More",
//                                isDay = {
//                                    isDay
//                                },
//                                modifier = modifier
//                            )

                            }
                        }

                        HurricaneMoonPhaseUIMain(
                            modifier = modifier,
                            currentForecastDayAstro = currentDayForecast.astro,
                            isDay = isDay
                        )
                    }


                    // Air Quality
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = animatedCardsColor
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(all = 10.dp)
                            .clickable {
                                onAirQualityMoreClick.invoke(currentAirQuality?.us_epa_index ?: 0)
                            }
                    ) {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Air Quality",
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = modifier
                                        .padding(top = 5.dp, start = 20.dp)
                                )
                                Spacer(modifier = modifier.weight(1f))
                                // Hurricane Fancy Card
                                FancyCard(
                                    text = "More",
                                    isDay = {
                                        isDay
                                    },
                                    modifier = modifier,
                                )

                            }
                        }

                        AirQualityUIMain(
                            modifier = modifier,
                            currentAirQuality = currentAirQuality!!
                        )
                    }

                } else {
                    repeat(3) {
                        ShimmerLoadingEffect(
                            shimmeringColor = Gray,
                            durationMillis = 2000,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .padding(all = 10.dp)
                                .clip(shape = RoundedCornerShape(10.dp))
                                .background(color = DarkCardBackgroundColor)
//                            .background(
//                                color = if (currentWeather != null) {
//                                    if (isDay) BrownDay else DarkBlue
//                                } else {
//                                    DarkCardBackgroundColor
//                                },
//                                shape = RoundedCornerShape(10.dp)
//                            )
                        )
                    }
                }


                // Extra Space
                Spacer(modifier = modifier.height(50.dp))

            }

        }



        if (isCustomLocationPermissionDialogOpen) {
            Dialog(
                onDismissRequest = {},
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(all = 20.dp)
                    ) {

                        Text(
                            text = context.getString(R.string.location_permission_error_msg),
                            style = TextStyle(
                                color = Black,
                                fontSize = 15.sp,
                                fontStyle = FontStyle.Italic
                            ),
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier
                                .fillMaxWidth()
                        ) {

                            // Cancel Button
                            TextButton(
                                onClick = {
                                    isCustomLocationPermissionDialogOpen = false
                                    scope.launch(Dispatchers.Default) {

                                        snackBarHostState.showSnackBarRequest(
                                            message = context.getString(R.string.location_permission_error_msg),
                                            actionLabel = "Settings",
                                            duration = SnackbarDuration.Indefinite,
                                            withDismissAction = true,
                                            onDismiss = {
                                                snackBarHostState.dismissCurrentSnackBar()
                                            },
                                            onActionPerformed = {
                                                context.goToPermissionSettings()
                                            }
                                        )

                                    }

                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Gray
                                ),
                                modifier = modifier
                                    .weight(1f)
                            ) {
                                Text(text = "Cancel")
                            }


                            VerticalDivider(modifier = modifier.height(ButtonDefaults.MinHeight))


                            // Settings Button
                            TextButton(
                                onClick = {
                                    isCustomLocationPermissionDialogOpen = false
                                    context.goToPermissionSettings()
                                },
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = Black
                                ),
                                modifier = modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    text = "Settings",
                                    fontWeight = FontWeight.Bold
                                )
                            }

                        }

                    }
                }
            }

        }

    }

}

// Top Section Main
@Composable
private fun TopSectionMain(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    context: Context,
    currentWeather: CurrentWeather?,
    currentDayForecast: ForecastDay?,
    isDay: () -> Boolean,
    onNextTwoHoursClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {

        if (currentWeather?.isDay != null) {

            val topImageResId = remember(currentWeather) {
                if (isDay())
                    R.drawable.weather_ui_top_day_image
                else R.drawable.weather_ui_top_night_image
            }

            Image(
                painter = painterResource(topImageResId),
                contentDescription = "Main Top Night Image",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .fillMaxWidth()
            )
        }


        currentWeather?.let {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = modifier
                    .padding(paddingValues = innerPadding)
                    .padding(top = 10.dp)
                    .align(Alignment.TopCenter)
            ) {
                // Condition
                Text(
                    text = currentWeather.condition.text,
                    style = TextStyle(
                        fontSize = 22.sp,
                        color = if (
                            currentWeather.isDay == WeatherRelated.IS_DAY
                        ) BrownDay else White,
                        fontWeight = FontWeight.Normal
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Temperature
                Text(
                    text = "${
                        (currentWeather.temperatureCelsius)
                            .takeIntIfAfterDecimalZero()
                    }${
                        context.getString(
                            R.string.degree_sign
                        )
                    }c",
                    style = TextStyle(
                        fontSize = 35.sp,
                        color = if (
                            currentWeather.isDay == WeatherRelated.IS_DAY
                        ) BrownDay else White,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        } ?: run {
            // Refreshing Text
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 30.sp,
                            color = White,
                            fontWeight = FontWeight.Bold

                        )
                    ) {
                        append("Refreshing...")
                    }
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier
                    .padding(paddingValues = innerPadding)
                    .padding(all = 10.dp)
            )
        }

    }


    if (currentWeather != null && currentDayForecast != null) {


        // Feels Like and Next Hour Forecast
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(all = 10.dp)
        ) {


            // Feels Like
            Row(
                horizontalArrangement = Arrangement.spacedBy(50.dp),
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                // Feels Like Text
                Text(
//                text = "Feels Like: 15${context.getString(R.string.degree_sign)}",
                    text = "Feels like: ${
                        (currentWeather.feelslikeCelsius).takeIntIfAfterDecimalZero()
                    }${
                        context.getString(
                            R.string.degree_sign
                        )
                    }",
                    color = White
                )

                val inlineMinMacTempContent = mapOf(
                    Pair(
                        InlineTextContentIds.MIN_TEMP,
                        InlineTextContent(
                            Placeholder(
                                width = 1.5.em,
                                height = 1.5.em,
                                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                            )
                        ) {
                            Image(
                                painter = painterResource(R.drawable.arrow_down_icon),
                                contentDescription = "Icon for Min Temp",
                            )
                        }
                    ),
                    Pair(
                        InlineTextContentIds.MAX_TEMP,
                        InlineTextContent(
                            Placeholder(
                                width = 1.5.em,
                                height = 1.5.em,
                                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                            )
                        ) {
                            Image(
                                painter = painterResource(R.drawable.arrow_up_icon),
                                contentDescription = "Icon for Max Temp",
                            )
                        }
                    )
                )

                // Min, Max Temp
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = White,
                                fontSize = 12.sp,
                            )
                        ) {
                            appendInlineContent(
                                id = InlineTextContentIds.MIN_TEMP,
                                alternateText = "[${InlineTextContentIds.MIN_TEMP}]"
                            )
                            append(" ") // Extra Space
//                        append("17${context.getString(R.string.degree_sign)}")
                            append(
                                "${
                                    (currentDayForecast.day.mintemp_c)
                                        .takeIntIfAfterDecimalZero()
                                }${
                                    context.getString(
                                        R.string.degree_sign
                                    )
                                }"
                            )
                        }
                        append("     ")
                        withStyle(
                            style = SpanStyle(
                                color = White,
                                fontSize = 12.sp
                            )
                        ) {
                            appendInlineContent(
                                id = InlineTextContentIds.MAX_TEMP,
                                alternateText = "[${InlineTextContentIds.MIN_TEMP}]"
                            )
                            append(" ") // Extra Space
//                        append("27${context.getString(R.string.degree_sign)}")
                            append(
                                "${
                                    (currentDayForecast.day.maxtemp_c)
                                        .takeIntIfAfterDecimalZero()
                                }${
                                    context.getString(
                                        R.string.degree_sign
                                    )
                                }"
                            )
                        }
                    },
                    inlineContent = inlineMinMacTempContent,
                )
            }

            // Next 2 hours forecast
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Yellow,
                            fontSize = 12.sp
                        )
                    ) {
                        append("Next 2 hours forecast")
                        append("  ") // Extra Space
                        appendInlineContent(
                            id = InlineTextContentIds.SIMPLE_ARROW_RIGHT,
                            alternateText = InlineTextContentIds.SIMPLE_ARROW_RIGHT
                        )
                    }
                },
                inlineContent = InlineTextContentIcon(
                    id = InlineTextContentIds.SIMPLE_ARROW_RIGHT,
                ),
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        onNextTwoHoursClick()
                    }
            )

        }

    }

}


// Hourly Forecast
@Composable
private fun HourlyForecastListItemMain(
    context: Context,
    modifier: Modifier = Modifier,
    forecastHour: ForecastHour,
    currentCityFormattedTime: String,
) {

    val hourlyForecastTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    val timeFormatter12Hours = DateTimeFormatter.ofPattern("hh:mm a")


    val forecastHoursLocalTime = try {
        LocalTime.parse(forecastHour.time, hourlyForecastTimeFormatter)
    } catch (e: Exception) {
        e.printStackTrace()
        LocalTime.now()
    }


    val formattedForecastHoursTimeString = timeFormatter12Hours.format(forecastHoursLocalTime)


    val isCurrentForecastHour by remember() {
        derivedStateOf {
            forecastHour.isCurrentForecastHour(currentCityFormattedTime)
        }
    }


    val conditionIconUrl = forecastHour.condition.icon.getFullUrlOfTempConditionIconFromApi()



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .padding(all = 20.dp)
    ) {

        Text(
            text = if (isCurrentForecastHour) "Now" else formattedForecastHoursTimeString,
            style = TextStyle(
                color = White,
                fontSize = 12.sp
            )
        )

        // Forecast Day Icon
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(conditionIconUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Forecast Hour Icon",
            contentScale = ContentScale.Fit,
            modifier = modifier
                .width(50.dp)
                .height(50.dp)
        )


        Text(
            text = "${forecastHour.temp_c.takeIntIfAfterDecimalZero()}${
                context.getString(R.string.degree_sign)
            }",
            style = TextStyle(
                color = White,
                fontSize = 12.sp
            )
        )

        Column {
            // Water Drop Icon (Rain)
            Image(
                painter = painterResource(R.drawable.water_drop_icon),
                contentDescription = "Forecast Hour Icon",
            )

            // Chance of Rain
            Text(
                text = "${forecastHour.chance_of_rain}%",
                style = TextStyle(
                    color = White,
                    fontSize = 12.sp
                )
            )
        }

    }

}


// Daily Forecast
@Composable
private fun DailyForecastListItemMain(
    context: Context,
    modifier: Modifier = Modifier,
    forecastDay: ForecastDay,
    currentLocalDateTime: LocalDateTime,
) {

    val dailyForecastDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val forecastDayLocalDate = try {
        LocalDate.parse(forecastDay.date, dailyForecastDateFormatter)
    } catch (e: Exception) {
        e.printStackTrace()
        LocalDate.now()
    }

    val weekName = forecastDayLocalDate.dayOfWeek.name.takeFirstThreeWithFirstUpper()

    val month = forecastDayLocalDate.month.value
    val day = forecastDayLocalDate?.dayOfMonth


    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 20.dp)
    ) {
        Text(
            text = if (
                forecastDayLocalDate.dayOfMonth == currentLocalDateTime.dayOfMonth
                && forecastDayLocalDate.month.value == currentLocalDateTime.month.value
            ) "Today"
            else if (
                forecastDayLocalDate.dayOfMonth == currentLocalDateTime.dayOfMonth + 1
                && forecastDayLocalDate.month.value == currentLocalDateTime.month.value
            ) "Tomorrow"
            else "$weekName\n$month/$day",
            style = TextStyle(
                fontSize = 12.sp,
                color = White
            )
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = modifier
                .weight(1f)
        ) {
            // Forecast Day Icon
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(forecastDay.day.condition.icon.getFullUrlOfTempConditionIconFromApi())
                    .crossfade(true)
                    .build(),
                contentDescription = "Forecast Day Icon",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .width(40.dp)
                    .height(40.dp)
            )

            Text(
                text = forecastDay.day.condition.text,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = White,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = LightBlue,
                        fontSize = 10.sp
                    )
                ) {
                    append("${forecastDay.day.mintemp_c}${context.getString(R.string.degree_sign)}")
                    append("    ") // Extra Space
                }
                withStyle(
                    style = SpanStyle(
                        color = Red,
                        fontSize = 10.sp
                    )
                ) {
                    append("${forecastDay.day.maxtemp_c}${context.getString(R.string.degree_sign)}")
                }
                append(" ") // Extra Space
                appendInlineContent(
                    id = InlineTextContentIds.SIMPLE_ARROW_RIGHT,
                    alternateText = InlineTextContentIds.SIMPLE_ARROW_RIGHT
                )
            },
            inlineContent = InlineTextContentIcon(
                id = InlineTextContentIds.SIMPLE_ARROW_RIGHT,
                iconColor = White
            )
        )

    }
}


// Details
@Composable
private fun DetailsUIMain(
    modifier: Modifier = Modifier,
    context: Context,
    currentWeather: CurrentWeather,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            val precipitation by remember {
                derivedStateOf {
                    currentWeather.precipitationInches
                }
            }
            // Precipitation
            InnerDetailUI(
//                value = "27%",
                value = { "${precipitation.takeIntIfAfterDecimalZero()}%" },
                icon = painterResource(R.drawable.precipitation_icon),
                label = "Precipitation",
            )

            val dewPoint by remember {
                derivedStateOf {
                    currentWeather.dewpointCelsius
                }
            }
            // Dew Point
            InnerDetailUI(
                value = {
                    "${dewPoint.takeIntIfAfterDecimalZero()}${
                        context.getString(
                            R.string.degree_sign
                        )
                    }"
                },
                icon = painterResource(R.drawable.temp_icon),
                label = "Dew Point",
            )

            val visibility by remember {
                derivedStateOf {
                    currentWeather.visibilityKilometers
                }
            }
            // Visibility
            InnerDetailUI(
                value = { "35.${visibility.takeIntIfAfterDecimalZero()} km" },
                icon = painterResource(R.drawable.visibility_icon),
                label = "Visibility"
            )
        }
        Spacer(modifier = modifier.weight(1f))
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            val uvIndex by remember {
                derivedStateOf {
                    currentWeather.uvIndex
                }
            }
            // UV Index
            InnerDetailUI(
                value = { uvIndex.takeIntIfAfterDecimalZero() },
                icon = painterResource(R.drawable.uv_index_icon),
                label = "UV Index"
            )

            val cloudCover by remember {
                derivedStateOf {
                    currentWeather.cloudCover
                }
            }
            // Cloud Cover
            InnerDetailUI(
                value = { "$cloudCover%" },
                icon = painterResource(R.drawable.cloud_icon),
                label = "Cloud Cover"
            )

            val humidity by remember {
                derivedStateOf {
                    currentWeather.humidity
                }
            }
            // Humidity
            InnerDetailUI(
                value = { "$humidity%" },
                icon = painterResource(R.drawable.humidity_icon),
                label = "Humidity"
            )
        }
    }

}


@Composable
fun InnerDetailUI(
    modifier: Modifier = Modifier,
    value: () -> String,
    icon: Painter,
    label: String,
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        // Icon Card
        SimpleCard(
            icon = icon,
            modifier = modifier
                .padding(all = 10.dp)
        )

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 15.sp,
                        color = White,
                    )
                ) {
                    appendLine(value())
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Gray
                    )
                ) {
                    appendLine(label)
                }
            },
            style = TextStyle(
                lineHeight = 1.em
            )
        )

    }
}


// Wind & Pressure
@Composable
private fun WindAndPressureUIMain(
    modifier: Modifier = Modifier,
    currentWeather: CurrentWeather,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 20.dp)
    ) {
        // Wind
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SimpleCard(
                icon = painterResource(R.drawable.wind_icon),
                modifier = modifier
                    .padding(all = 10.dp)
            )

            Text(
                text = buildAnnotatedString {
                    // Wind Value
                    withStyle(
                        style = SpanStyle(
                            color = White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
//                        append("4.0")
                        append("${currentWeather.windMph}")
                    }
                    // mph Text
                    withStyle(
                        style = SpanStyle(
                            color = White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("  ") // Extra Space
                        append("mph")
                    }
                    // Name
                    withStyle(
                        style = SpanStyle(
                            fontSize = 12.sp,
                            color = Gray
                        )
                    ) {
                        append("")
                        append("\n${currentWeather.windChillCelsius} Beaufort")
                    }
                },
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )
        }

        Row {
            // Pressure
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                SimpleCard(
                    icon = painterResource(R.drawable.pressure_icon),
                    modifier = modifier
                        .padding(all = 5.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 15.sp,
                                color = White,
                            )
                        ) {
//                            append("1013 mbar")
                            append("${currentWeather.pressureInches}")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = Gray
                            )
                        ) {
                            append("\nPressure")
                        }
                    },
                    style = TextStyle(
                        lineHeight = 1.em
                    )
                )
            }

            Spacer(modifier.weight(1f))

            // Wind Direction
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                SimpleCard(
                    icon = painterResource(R.drawable.wind_direction_icon),
                    modifier = modifier
                        .padding(all = 5.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 15.sp,
                                color = White,
                            )
                        ) {
//                            append("SW")
                            append(currentWeather.windDirection)
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = Gray
                            )
                        ) {
                            append("\nWind Direction")
                        }
                    },
                    style = TextStyle(
                        lineHeight = 1.em
                    )
                )
            }

        }

    }

}


// Hurricane - Category 5
@Composable
fun HurricaneCategory5UI(
    modifier: Modifier = Modifier,
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 20.dp)
    ) {
        // Milton
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
//            SimpleCard(
//                icon = painterResource(R.drawable.hurricane_icon),
//                modifier = modifier
//                    .padding(all = 10.dp)
//            )

            Text(
                text = buildAnnotatedString {
                    // Hurricane Value
                    withStyle(
                        style = SpanStyle(
                            color = White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Milton")
                    }
                    // Name
                    withStyle(
                        style = SpanStyle(
                            fontSize = 12.sp,
                            color = Gray
                        )
                    ) {
                        append("\nHurricane - Category 5")
                    }
                },
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )
        }

        Row {
            // Movement
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                SimpleCard(
                    icon = painterResource(R.drawable.movement_icon),
                    modifier = modifier
                        .padding(all = 5.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 15.sp,
                                color = White,
                            )
                        ) {
                            append("E")
//                            append("${currentWeather}")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = Gray
                            )
                        ) {
                            append("\nMovement")
                        }
                    },
                    style = TextStyle(
                        lineHeight = 1.em
                    )
                )
            }

            Spacer(modifier.weight(1f))

            // Distance
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                SimpleCard(
                    icon = painterResource(R.drawable.distance_icon),
                    modifier = modifier
                        .padding(all = 5.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 15.sp,
                                color = White,
                            )
                        ) {
                            append("3004.11km")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = Gray
                            )
                        ) {
                            append("\nDistance")
                        }
                    },
                    style = TextStyle(
                        lineHeight = 1.em
                    )
                )
            }

        }

    }
}


// Google Maps (User's Current Location or Selected City's Location)
@Composable
private fun GoogleMapsUIMain(
    modifier: Modifier = Modifier,
    mLatLng: () -> LatLng,
    isLocationCoordinatesShifted: Boolean,
    currentCityLocationLatLng: () -> LatLng,
) {
    val markerState = rememberMarkerState()

    val cameraPositionState = rememberCameraPositionState()

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                scrollGesturesEnabled = true,
                zoomGesturesEnabled = true,
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false
            )
        )
    }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
                isMyLocationEnabled = false,
            )
        )
    }


    LaunchedEffect(isLocationCoordinatesShifted, mLatLng) {
        if (!isLocationCoordinatesShifted) {
            markerState.position = mLatLng()
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(mLatLng(), 14f)
                ),
                durationMs = 200
            )
        } else {
            markerState.position = currentCityLocationLatLng()
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(currentCityLocationLatLng(), 14f)
                ),
                durationMs = 200
            )
        }
    }


    GoogleMap(
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiSettings,
        properties = mapProperties,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(all = 10.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        Marker(
            state = markerState,
        )
    }

}


// Hurricane Moon Phase
@Composable
private fun HurricaneMoonPhaseUIMain(
    modifier: Modifier = Modifier,
    currentForecastDayAstro: WeatherAstronomy,
    isDay: Boolean,
) {

    val moonPhase = currentForecastDayAstro.moon_phase

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            val moonPhaseIconResId = remember(currentForecastDayAstro) {
                if (moonPhase == MoonPhase.FIRST_QUARTER)
                    R.drawable.first_quarter_icon
                else if (moonPhase == MoonPhase.FULL_MOON)
                    R.drawable.full_moon_icon
                else R.drawable.moon_phase_icon
            }

            SimpleCard(
                icon = painterResource(moonPhaseIconResId),
                modifier = modifier
                    .padding(all = 10.dp)
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
//                        append("Waxing Crescent")
                        append(currentForecastDayAstro.moon_phase)
                    }

                    withStyle(
                        style = SpanStyle(
                            fontSize = 12.sp,
                            color = Gray
                        )
                    ) {
                        append("\nMoon Phase")
                    }
                },
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )
        }

        val sunriseTime by remember(currentForecastDayAstro) {
            derivedStateOf {
                currentForecastDayAstro.sunrise.toLowerCase()
            }
        }
        val sunsetTime by remember(currentForecastDayAstro) {
            derivedStateOf {
                currentForecastDayAstro.sunset.toLowerCase()
            }
        }


        Row(
            modifier = modifier
                .fillMaxWidth()
        ) {
            // Sunrise or MoonSet
//            Column(
//                verticalArrangement = Arrangement.spacedBy(5.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//
//            ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        appendLine(if (isDay) sunriseTime else sunsetTime)
                    }

                    withStyle(
                        style = SpanStyle(
                            color = White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        appendLine(
                            if (isDay)
                                "Sunrise"
                            else "Sunset"
                        )
                    }
                },
            )
//            }

            Spacer(modifier = modifier.weight(1f))

            // Sunset or Moonrise
//            Column(
//                verticalArrangement = Arrangement.spacedBy(5.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//
//            ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        appendLine(if (isDay) sunsetTime else sunriseTime)
                    }

                    withStyle(
                        style = SpanStyle(
                            color = White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        appendLine(
                            if (isDay)
                                "Sunset"
                            else "Sunrise"
                        )
                    }
                },
            )
//            }

        }


//        Row {
//            // First Quarter
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(5.dp)
//            ) {
//                SimpleCard(
//                    icon = painterResource(R.drawable.first_quarter_icon),
//                    modifier = modifier
//                        .padding(all = 5.dp)
//                )
//                Text(
//                    text = buildAnnotatedString {
//                        withStyle(
//                            style = SpanStyle(
//                                fontSize = 15.sp,
//                                color = White,
//                            )
//                        ) {
//                            append("10 / 11")
//                        }
//                        withStyle(
//                            style = SpanStyle(
//                                fontSize = 12.sp,
//                                fontWeight = FontWeight.Normal,
//                                color = Gray
//                            )
//                        ) {
//                            append("\nFirst Quarter (Pending)")
//                        }
//                    },
//                    style = TextStyle(
//                        lineHeight = 1.em
//                    )
//                )
//            }
//
//            Spacer(modifier.weight(1f))
//
//            // Full Moon
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(5.dp)
//            ) {
//                SimpleCard(
//                    icon = painterResource(R.drawable.full_moon_icon),
//                    modifier = modifier
//                        .padding(all = 5.dp)
//                )
//                Text(
//                    text = buildAnnotatedString {
//                        withStyle(
//                            style = SpanStyle(
//                                fontSize = 15.sp,
//                                color = White,
//                            )
//                        ) {
//                            append("10 / 18")
//                        }
//                        withStyle(
//                            style = SpanStyle(
//                                fontSize = 12.sp,
//                                fontWeight = FontWeight.Normal,
//                                color = Gray
//                            )
//                        ) {
//                            append("\nFull Moon (Pending)")
//                        }
//                    },
//                    style = TextStyle(
//                        lineHeight = 1.em
//                    )
//                )
//            }
//
//        }
//
//
//        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
//
//        val localTimeToday = LocalTime.now()
//
//
//        val sunrise = currentForecastDayAstro.sunrise.toLowerCase()
//        val sunset = currentForecastDayAstro.sunset.toLowerCase()
//
//        val moonSet = currentForecastDayAstro.moonset.toLowerCase()
//        val moonrise = currentForecastDayAstro.moonrise.toLowerCase()
//
//
//        val sunriseTime = try {
//            LocalTime.parse(sunrise, timeFormatter)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            localTimeToday
//        }
//
//        val sunsetTime = try {
//            LocalTime.parse(sunset, timeFormatter)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            localTimeToday
//        }
//
//        val moonSetTime = try {
//            LocalTime.parse(moonSet, timeFormatter)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            localTimeToday
//        }
//
//        val moonriseTime = try {
//            LocalTime.parse(moonrise, timeFormatter)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            localTimeToday
//        }
//
//
//        val currentTime = try {
//            LocalTime.parse(currentTimeString, timeFormatter)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            localTimeToday
//        }
//
//        val totalDayMinutes = sunriseTime.until(sunsetTime, ChronoUnit.MINUTES).toFloat()
//        val minutesSinceSunrise = sunriseTime.until(currentTime, ChronoUnit.MINUTES).toFloat()
//        val arcPercentage = (minutesSinceSunrise / totalDayMinutes).coerceIn(0f, 1f)
//
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//            Box(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .height(180.dp)
//            ) {
//                Canvas(
//                    modifier = modifier
//                        .fillMaxSize()
//                ) {
//
//                    val arcSize = Size(size.width, size.height * 1f)
//
//                    // Draw the arc
//                    drawArc(
//                        color = Yellow, // Light gray for the arc
//                        startAngle = 180f,
//                        sweepAngle = 180f,
//                        useCenter = false,
//                        size = arcSize,
//                        style = Stroke(width = 1.dp.toPx())
//                    )
//
//                    val sunX = arcSize.width * arcPercentage
//                    val sunY = arcSize.height * (1 - sin(Math.PI * arcPercentage).toFloat())
//
//                    // Draw the sun
//                    drawCircle(
//                        color = Yellow,
//                        radius = 10.dp.toPx(),
//                        center = Offset(sunX, sunY)
//                    )
//
//
//                    val gradientBrush = Brush.verticalGradient(
//                        colors = listOf<Color>(
//                            Yellow.copy(0.1f),
//                            White.copy(0.5f),
//                        ),
////                        startY = sunY,
////                        endY = size.height /*+ 80.dp.toPx()*/
//                    )
//
//                    // Draw the SUn Rays Shadow
//                    drawArc(
//                        brush = gradientBrush,
//                        startAngle = 180f,
//                        sweepAngle = 180f,
//                        useCenter = true,
//                        size = arcSize.copy(width = size.width * arcPercentage)
//                    )
//
//                    // Draw yellow progress rays confined within the semicircular area
////                val rayCount = 100
////                val rayLength = arcHeight // Rays extend up to the arc's bottom boundary
////                for (i in 0 until (rayCount * progress).toInt()) {
////                    val rayAngle = Math.toRadians(180.0 + (180.0 / rayCount) * i)
////                    val rayEndX = arcWidth / 2 + (arcWidth / 2 * cos(rayAngle)).toFloat()
////                    val rayEndY = arcHeight * (1 - sin(rayAngle)).toFloat()
////
////                    drawLine(
////                        color = Yellow,
////                        start = Offset(arcWidth / 2, arcHeight),
////                        end = Offset(rayEndX, rayEndY),
////                        strokeWidth = 2.dp.toPx()
////                    )
////                }
//
//                }
//            }
//
//            // Add Sunrise and Sunset Labels
//            Row(
//                modifier = modifier
//                    .fillMaxWidth(),
//            ) {
//                Text(
//                    text = if (isDay == WeatherRelated.IS_DAY)
//                        sunrise else moonSet,
//                    style = TextStyle(
//                        color = Color.White,
//                        fontSize = 12.sp
//                    ),
//                )
//                Spacer(modifier = modifier.weight(1f))
//                Text(
//                    text = if (isDay == WeatherRelated.IS_DAY)
//                        sunset else moonrise,
//                    style = TextStyle(
//                        color = Color.White,
//                        fontSize = 12.sp
//                    ),
//                )
//            }
//        }

    }
}

@Composable
private fun AirQualityUIMain(
    modifier: Modifier = Modifier,
    currentAirQuality: WeatherAirQuality,
) {

    val airQuality = currentAirQuality.us_epa_index // Get the AQI from the US EPA system

    val airQualityLevel = AirQualityRelated.getAirQualityLevel(airQuality)

    val airQualityLevelDescription = AirQualityRelated.getAirQualityLevelDescription(airQuality)

    val airQualityColor = AirQualityRelated.getAirQualityLevelColor(airQuality)


    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SimpleCard(
                icon = painterResource(R.drawable.air_quality_icon),
                modifier = modifier
                    .padding(all = 10.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = modifier
                    .align(Alignment.CenterVertically)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "$airQuality",
                        style = TextStyle(
                            color = White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    // Status Container
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = airQualityColor,
                            contentColor = White
                        ),
                        shape = RoundedCornerShape(5.dp),
                        modifier = modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        // Air Quality Level
                        Text(
                            text = airQualityLevel,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = White,
                            ),
                            modifier = modifier
                                .padding(start = 10.dp, end = 10.dp)
                        )
                    }
                }

                // Air Quality Level Description
                Text(
                    text = airQualityLevelDescription,
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = Gray
                    )
                )
            }

        }

        val airQualityColorsMap = listOf(
            Pair(15f, Green), // AirQualityRelated.GOOD
            Pair(10f, Yellow), // AirQualityRelated.MODERATE
            Pair(10f, Orange), // AirQualityRelated.SENSITIVE
            Pair(10f, Red), // AirQualityRelated.UNHEALTHY
            Pair(20f, Purple), // AirQualityRelated.VERY_UNHEALTHY
            Pair(30f, Brown), // AirQualityRelated.HAZARDOUS
        )

        // Air Quality Colors Line

        Column {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .height(2.dp)
            ) {

                airQualityColorsMap.forEach { item ->

                    Box(
                        modifier = modifier
                            .weight(item.first)
                            .fillMaxHeight()
                            .background(color = item.second)
                    )

                }

            }

            Spacer(modifier = modifier.height(2.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxWidth()
            ) {
                // AirQualityRelated.GOOD
                Text(
                    text = AirQualityRelated.GOOD,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Gray
                    )
                )

                // AirQualityRelated.HAZARDOUS
                Text(
                    text = AirQualityRelated.HAZARDOUS,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Gray
                    )
                )
            }

        }


        Row(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val pm25 by remember {
                    derivedStateOf {
                        currentAirQuality.pm2_5
                    }
                }
                // Pm 2.5
                InnerDetailUI(
                    value = { "${pm25.toInt()}" },
                    icon = painterResource(R.drawable.pm_25_icon),
                    label = AirQualityRelated.getAirQualityLevel(currentAirQuality.pm2_5.toInt())
                )

                val co by remember {
                    derivedStateOf {
                        currentAirQuality.co
                    }
                }
                // Co
                InnerDetailUI(
                    value = { "${co.toInt()}" },
                    icon = painterResource(R.drawable.co_icon),
                    label = AirQualityRelated.getAirQualityLevel(currentAirQuality.co.toInt())
                )

                val so2 by remember {
                    derivedStateOf {
                        currentAirQuality.so2
                    }
                }
                // So 2
                InnerDetailUI(
                    value = { "${so2.toInt()}" },
                    icon = painterResource(R.drawable.so_2_icon),
                    label = AirQualityRelated.getAirQualityLevel(currentAirQuality.so2.toInt())
                )
            }
            Spacer(modifier = modifier.weight(1f))
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                val pm10 by remember {
                    derivedStateOf {
                        currentAirQuality.pm10
                    }
                }
                // PM 10
                InnerDetailUI(
                    value = { "${pm10.toInt()}" },
                    icon = painterResource(R.drawable.pm_10_icon),
                    label = AirQualityRelated.getAirQualityLevel(currentAirQuality.pm10.toInt())
                )


                val no2 by remember {
                    derivedStateOf {
                        currentAirQuality.no2
                    }
                }
                // No 2
                InnerDetailUI(
                    value = { "${no2.toInt()}" },
                    icon = painterResource(R.drawable.no_2_icon),
                    label = AirQualityRelated.getAirQualityLevel(currentAirQuality.no2.toInt())
                )

                val o3 by remember {
                    derivedStateOf {
                        currentAirQuality.o3
                    }
                }
                // O 3
                InnerDetailUI(
                    value = { "${o3.toInt()}" },
                    icon = painterResource(R.drawable.o_3_icon),
                    label = AirQualityRelated.getAirQualityLevel(currentAirQuality.o3.toInt())
                )
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun HuricaneMoonPhaseUiPreview() {
    WeatherUITheme {

    }
}