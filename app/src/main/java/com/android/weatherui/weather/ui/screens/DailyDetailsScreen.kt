package com.android.weatherui.weather.ui.screens

import com.android.weatherui.R
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.android.weatherui.common.domain.extensions.getFullUrlOfTempConditionIconFromApi
import com.android.weatherui.common.domain.extensions.takeFirstThreeWithFirstUpper
import com.android.weatherui.takeIntIfAfterDecimalZero
import com.android.weatherui.ui.theme.DarkBackgroundColor
import com.android.weatherui.ui.theme.DarkCardBackgroundColor
import com.android.weatherui.ui.theme.Gray
import com.android.weatherui.ui.theme.White
import com.android.weatherui.weather.domain.constants.WeatherRelated
import com.android.weatherui.weather.domain.models.weather_related.ForecastDay
import com.android.weatherui.weather.ui.common_composables.ShimmerLoadingEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyDetailsScreen(
    modifier: Modifier = Modifier,
    forecastDaysList: List<ForecastDay>,
    selectedForecastDayIndex: Int?,
    navHostController: NavHostController,
) {

    val context = navHostController.context
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current


    var selectedForecastDay by remember(selectedForecastDayIndex) {
        mutableStateOf<ForecastDay?>(forecastDaysList[selectedForecastDayIndex ?: 0])
    }

    var selectedForecastDate by remember() {
        mutableStateOf(selectedForecastDay?.date ?: "")
    }

    LaunchedEffect(selectedForecastDayIndex, selectedForecastDay) {
        println("Forecast Day: Given Forecast Day: $selectedForecastDayIndex")
        println("Forecast Day: Selected Forecast Day: $selectedForecastDay")
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Daily Details",
                        style = TextStyle(
                            fontSize = 18.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Icon for Navigate Back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackgroundColor,
                    titleContentColor = White,
                    navigationIconContentColor = White,
                )
            )
        },
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .background(color = DarkBackgroundColor)
                .verticalScroll(rememberScrollState())
        ) {

            LazyRow {

                items(forecastDaysList.size) { index ->

                    val forecastDay = forecastDaysList[index]


                    val dailyDetailsDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                    val localDate = try {
                        LocalDate.parse(forecastDay.date, dailyDetailsDateTimeFormatter)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        LocalDate.now()
                    }

                    val month = localDate.month.value
                    val day = localDate.dayOfMonth
                    val weekName = localDate.dayOfWeek.name.takeFirstThreeWithFirstUpper()


                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (
                                forecastDay.date == selectedForecastDate
                            ) DarkCardBackgroundColor else Color.Transparent,
                            contentColor = White
                        ),
                        shape = RoundedCornerShape(5.dp),
                        modifier = modifier
                            .padding(all = 5.dp)
                            .clickable {
                                selectedForecastDate = forecastDay.date
                                scope.launch(Dispatchers.Default) {
                                    selectedForecastDay = null
                                    delay(2000)
                                    selectedForecastDay = forecastDaysList[index]
                                }
                            }
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                // Month & Day
                                withStyle(style = SpanStyle(
                                    color = White,
                                    fontSize = 15.sp
                                )) {
                                    append("$month/$day")
                                }
                                // Week Name
                                withStyle(style = SpanStyle(
                                    color = Gray,
                                    fontSize = 12.sp
                                )) {
                                    append("\n$weekName")
                                }
                            },
                            style = TextStyle(
                                textAlign = TextAlign.Center
                            ),
                            modifier = modifier
                                .padding(all = 10.dp)
                        )
                    }

                }
            }


            // Details
            selectedForecastDay?.let {

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = DarkCardBackgroundColor
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp)
                ) {
                    Column {

                        // Day
                        Box {
                            Text(
                                text = "Day",
                                style = TextStyle(
                                    color = White,
                                    fontSize = 20.sp
                                ),
                                modifier = modifier
                                    .align(Alignment.TopStart)
                                    .padding(all = 10.dp)
                            )

                            DayWeatherDetails(
                                modifier = modifier,
                                forecastDay = selectedForecastDay!!,
                                context = context
                            )
                        }

                        HorizontalDivider(
                            color = Gray,
                            thickness = 2.dp,
                            modifier = modifier.padding(all = 5.dp)
                        )

                        // Night
                        Box {
                            Text(
                                text = "Night",
                                style = TextStyle(
                                    color = White,
                                    fontSize = 20.sp
                                ),
                                modifier = modifier
                                    .align(Alignment.TopStart)
                                    .padding(all = 10.dp)
                            )

                            NightWeatherDetails(
                                modifier = modifier,
                                forecastDay = selectedForecastDay!!,
                                context = context
                            )
                        }

                    }

                }

            } ?: run {
                ShimmerLoadingEffect(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(configuration.screenHeightDp.dp)
                        .padding(all = 10.dp)
                        .background(
                            color = DarkCardBackgroundColor,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    shimmeringColor = DarkBackgroundColor,
                    durationMillis = 700
                )
            }


        }

    }

}

@Composable
fun DayWeatherDetails(
    modifier: Modifier = Modifier,
    context: Context,
    forecastDay: ForecastDay,
) {

    // Forecast Hours List WHere isDay Day == 1 (mean day Hours)
    val forecastDayHoursList by remember(forecastDay) {
        derivedStateOf {
            forecastDay.hour.filter { it.is_day == 1 }
        }
    }
    // Average Temperature Feels Like of Day Hours
    val avgTempFeelsLikeCel by remember(forecastDayHoursList) {
        derivedStateOf {
            forecastDayHoursList.map { it.feelslike_c }.average()
        }
    }
    // Average Wind in Mph of Day Hours
    val avgWindMph by remember(forecastDayHoursList) {
        derivedStateOf {
            forecastDayHoursList.map { it.wind_mph }.average()
        }
    }
    // Average Wind Gust in Mph of Day Hours
    val avgWindGustMph by remember(forecastDayHoursList) {
        derivedStateOf {
            forecastDayHoursList.map { it.gust_mph }.average()
        }
    }
    // Average UV Index of Day Hours
    val avgUvIndex by remember(forecastDayHoursList) {
        derivedStateOf {
            forecastDayHoursList.map { it.uv }.average()
        }
    }
    // Average Precipitation of Day Hours
    val avgPrecipitationInch by remember(forecastDayHoursList) {
        derivedStateOf {
            forecastDayHoursList.map { it.precip_in }.average()
        }
    }
    // Average Cloud Cover of Day Hours
    val avgCloudCover by remember(forecastDayHoursList) {
        derivedStateOf {
            forecastDayHoursList.map { it.cloud }.average()
        }
    }


    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
    ) {

        // Day Icon, Temp, Condition Text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = modifier
                .align(Alignment.CenterHorizontally)
        ) {
            // Day Condition Icon
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(forecastDay.day.condition.icon.getFullUrlOfTempConditionIconFromApi())
                    .crossfade(true)
                    .build(),
                contentDescription = "Day Condition Icon of Average Temp",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .width(50.dp)
                    .height(50.dp)
            )

            // Temp
            Text(
                text = "${(forecastDay.day.avgtemp_c).takeIntIfAfterDecimalZero()}${
                    context.getString(R.string.degree_sign)
                }C",
                style = TextStyle(
                    color = White,
                    fontSize = 20.sp
                )
            )

            // Condition Text
            Text(
                text = forecastDay.day.condition.text,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = White
                )
            )

        }

        /* Details */

        // Feels Like
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // icon
            Icon(
                painter = painterResource(R.drawable.temp_icon),
                contentDescription = null,
                tint = Gray
            )

            // Label
            Text(
                text = "Feels like",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray
                ),
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = modifier.weight(1f))

            // Temp Feels Like Value

            Text(
//                text = "${forecastDay.day.maxtemp_f}${context.getString(R.string.degree_sign)}",
                text = "${
                    avgTempFeelsLikeCel.takeRoundedValue()
                        .takeIntIfAfterDecimalZero()
                }${
                    context.getString(R.string.degree_sign)
                }",
                style = TextStyle(
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }

        // Wind
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // icon
            Icon(
                painter = painterResource(R.drawable.air_quality_icon),
                contentDescription = null,
                tint = Gray,
                modifier = modifier
                    .width(20.dp)
                    .height(20.dp)
            )

            // Label
            Text(
                text = "Wind",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray
                ),
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = modifier.weight(1f))

            // Wind Value

            Text(
//                text = "${forecastDay.day.maxwind_mph}mph",
                text = "${
                    avgWindMph.takeRoundedValue()
                        .takeIntIfAfterDecimalZero()
                }mph",
                style = TextStyle(
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }

        // Wind Gust
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // icon
            Icon(
                painter = painterResource(R.drawable.air_quality_icon),
                contentDescription = null,
                tint = Gray,
                modifier = modifier
                    .width(20.dp)
                    .height(20.dp)
            )

            // Label
            Text(
                text = "Wind Gust",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray
                ),
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = modifier.weight(1f))

            // Wind Gust Value

            Text(
                text = "${
                    avgWindGustMph.takeRoundedValue()
                        .takeIntIfAfterDecimalZero()
                }mph",
                style = TextStyle(
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }

        // UV Index
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // icon
            Icon(
                painter = painterResource(R.drawable.uv_index_icon),
                contentDescription = null,
                tint = Gray
            )

            // Label
            Text(
                text = WeatherRelated.UV_INDEX,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray
                ),
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = modifier.weight(1f))

            // UV Index Value

            Text(
//                text = "${forecastDay.day.uv.takeIntIfAfterDecimalZero()}%",
                text = "${
                    avgUvIndex.takeRoundedValue()
                        .takeIntIfAfterDecimalZero()
                }%",
                style = TextStyle(
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }

        // Precipitation
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // icon
            Icon(
                painter = painterResource(R.drawable.precipitation_icon),
                contentDescription = null,
                tint = Gray
            )

            // Label
            Text(
                text = WeatherRelated.PRECIPITATION,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray
                ),
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = modifier.weight(1f))

            // Precipitation Value
            Text(
//                text = "${forecastDay.day.totalprecip_in.takeIntIfAfterDecimalZero()}%",
                text = "${
                    avgPrecipitationInch.takeRoundedValue()
                        .takeIntIfAfterDecimalZero()
                }%",
                style = TextStyle(
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }

        // Cloud Cover
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // icon
            Icon(
                painter = painterResource(R.drawable.cloud_icon),
                contentDescription = null,
                tint = Gray
            )

            // Label
            Text(
                text = WeatherRelated.CLOUD_COVER,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray
                ),
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = modifier.weight(1f))

            // Cloud Cover Value
            Text(
                text = "${
                    avgCloudCover.takeRoundedValue()
                        .takeIntIfAfterDecimalZero()
                }%",
                style = TextStyle(
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }

    }

}


@Composable
fun NightWeatherDetails(
    modifier: Modifier = Modifier,
    context: Context,
    forecastDay: ForecastDay,
) {

    // Forecast Hours List WHere isDay Day == 0 (mean Night Hours)
    val forecastNightHoursList by remember(forecastDay) {
        derivedStateOf {
            forecastDay.hour.filter { it.is_day == 0 }
        }
    }
    // Average Temperature of Night Hours
    val avgTempCel by remember(forecastNightHoursList) {
        derivedStateOf {
            forecastNightHoursList.map { it.temp_c }.average()
        }
    }
    // Temp Condition of Average Temp
    val avgTempConditionText by remember(forecastNightHoursList) {
        derivedStateOf {
            forecastNightHoursList.firstOrNull {
                it.temp_c.toInt() == avgTempCel.toInt()
            }?.condition?.text ?: forecastNightHoursList.first().condition.text
        }
    }

    // Temp Condition Icon of Average Temp
    val avgTempConditionIconUrl by remember(forecastNightHoursList) {
        derivedStateOf {
            forecastNightHoursList.firstOrNull {
                it.temp_c.toInt() == avgTempCel.toInt()
            }?.condition?.icon?.getFullUrlOfTempConditionIconFromApi()
                ?: forecastNightHoursList.first().condition.icon.getFullUrlOfTempConditionIconFromApi()
        }
    }


    // Average Temperature Feels Like of Night Hours
    val avgTempFeelsLikeCel by remember(forecastNightHoursList) {
        derivedStateOf {
            forecastNightHoursList.map { it.feelslike_c }.average()
        }
    }
    // Average Wind in Mph of Night Hours
    val avgWindMph by remember(forecastNightHoursList) {
        derivedStateOf {
            forecastNightHoursList.map { it.wind_mph }.average()
        }
    }
    // Average Wind Gust in Mph of Night Hours
    val avgWindGustMph by remember(forecastNightHoursList) {
        derivedStateOf {
            forecastNightHoursList.map { it.gust_mph }.average()
        }
    }
    // Average UV Index of Night Hours
    val avgUvIndex by remember(forecastNightHoursList) {
        derivedStateOf {
            forecastNightHoursList.map { it.uv }.average()
        }
    }
    // Average Precipitation of Night Hours
    val avgPrecipitationInch by remember(forecastNightHoursList) {
        derivedStateOf {
            forecastNightHoursList.map { it.precip_in }.average()
        }
    }
    // Average Cloud Cover of Night Hours
    val avgCloudCover by remember(forecastNightHoursList) {
        derivedStateOf {
            forecastNightHoursList.map { it.cloud }.average()
        }
    }


    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
    ) {

        // Day Icon, Temp, Condition Text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = modifier
                .align(Alignment.CenterHorizontally)
        ) {
            // Night Condition Icon
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(avgTempConditionIconUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Night Condition Icon of Average Temp",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .width(70.dp)
                    .height(70.dp)
            )

            // Temp
            Text(
//                    text = "${(forecastDay?.day?.avgtemp_c ?: 0.0).takeIntIfAfterDecimalZero()}${
                text = "${
                    avgTempCel.takeRoundedValue()
                        .takeIntIfAfterDecimalZero()
                }${context.getString(R.string.degree_sign)}C",
                style = TextStyle(
                    color = White,
                    fontSize = 20.sp
                )
            )

            // Condition Text
            Text(
//                text = forecastDay?.day?.condition?.text ?: "",
                text = avgTempConditionText,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = White
                )
            )

        }

        /* Details */

        // Feels Like
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // icon
            Icon(
                painter = painterResource(R.drawable.temp_icon),
                contentDescription = null,
                tint = Gray
            )

            // Label
            Text(
                text = "Feels like",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray
                ),
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = modifier.weight(1f))

            // Temp Feels Like Value
            Text(
//                text = "${forecastDay.day.maxtemp_f}${context.getString(R.string.degree_sign)}",
                text = "${
                    avgTempFeelsLikeCel.takeRoundedValue()
                        .takeIntIfAfterDecimalZero()
                }${
                    context.getString(R.string.degree_sign)
                }",
                style = TextStyle(
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }

        // Wind
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // icon
            Icon(
                painter = painterResource(R.drawable.air_quality_icon),
                contentDescription = null,
                tint = Gray,
                modifier = modifier
                    .width(20.dp)
                    .height(20.dp)
            )

            // Label
            Text(
                text = "Wind",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray
                ),
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = modifier.weight(1f))

            // Wind Value
            Text(
//                text = "${forecastDay.day.maxwind_mph}mph",
                text = "${
                    avgWindMph.takeRoundedValue()
                        .takeIntIfAfterDecimalZero()
                }mph",
                style = TextStyle(
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }

        // Wind Gust
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // icon
            Icon(
                painter = painterResource(R.drawable.air_quality_icon),
                contentDescription = null,
                tint = Gray,
                modifier = modifier
                    .width(20.dp)
                    .height(20.dp)
            )

            // Label
            Text(
                text = "Wind Gust",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray
                ),
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = modifier.weight(1f))

            // Wind Gust Value
            Text(
                text = "${
                    avgWindGustMph.takeRoundedValue()
                        .takeIntIfAfterDecimalZero()
                }mph",
                style = TextStyle(
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }

        // UV Index
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // icon
            Icon(
                painter = painterResource(R.drawable.uv_index_icon),
                contentDescription = null,
                tint = Gray
            )

            // Label
            Text(
                text = WeatherRelated.UV_INDEX,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray
                ),
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = modifier.weight(1f))

            // UV Index Value
            Text(
//                text = "${forecastDay.day.uv.takeIntIfAfterDecimalZero()}%",
                text = "${
                    avgUvIndex.takeRoundedValue()
                        .takeIntIfAfterDecimalZero()
                }%",
                style = TextStyle(
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }

        // Precipitation
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // icon
            Icon(
                painter = painterResource(R.drawable.precipitation_icon),
                contentDescription = null,
                tint = Gray
            )

            // Label
            Text(
                text = WeatherRelated.PRECIPITATION,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray
                ),
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = modifier.weight(1f))

            // Precipitation Value
            Text(
//                text = "${forecastDay.day.totalprecip_in.takeIntIfAfterDecimalZero()}%",
                text = "${
                    avgPrecipitationInch.takeRoundedValue()
                        .takeIntIfAfterDecimalZero()
                }%",
                style = TextStyle(
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }

        // Cloud Cover
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // icon
            Icon(
                painter = painterResource(R.drawable.cloud_icon),
                contentDescription = null,
                tint = Gray
            )

            // Label
            Text(
                text = WeatherRelated.CLOUD_COVER,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray
                ),
                modifier = modifier
                    .align(Alignment.CenterVertically)
            )

            Spacer(modifier = modifier.weight(1f))

            // Actual Value
            Text(
                text = "${
                    avgCloudCover.takeRoundedValue()
                        .takeIntIfAfterDecimalZero()
                }%",
                style = TextStyle(
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }

    }

}

inline fun Double.takeRoundedValue(
    crossinline rangeAfterDecimal: () -> Int = { 1 },
): Double {
    val doubleValue = toDouble()

    val roundedValue = BigDecimal(doubleValue).setScale(
        rangeAfterDecimal(),
        RoundingMode.HALF_UP
    ).toDouble()

    return roundedValue
}