package com.android.weatherui.weather.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import com.android.weatherui.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.android.weatherui.common.ui.ProgressIndicator
import com.android.weatherui.common.domain.extensions.getFullUrlOfTempConditionIconFromApi
import com.android.weatherui.common.domain.extensions.takeFirstThreeWithFirstUpper
import com.android.weatherui.takeIntIfAfterDecimalZero
import com.android.weatherui.ui.theme.DarkBackgroundColor
import com.android.weatherui.ui.theme.DarkCardBackgroundColor
import com.android.weatherui.ui.theme.Gray
import com.android.weatherui.ui.theme.Red
import com.android.weatherui.ui.theme.White
import com.android.weatherui.weather.domain.constants.WeatherRelated
import com.android.weatherui.weather.domain.models.weather_related.ForecastDay
import com.android.weatherui.weather.domain.models.weather_related.ForecastHour
import com.android.weatherui.weather.domain.models.weather_related.isCurrentForecastHour
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HourlyForecastScreen(
    modifier: Modifier = Modifier,
    currentCityFormattedTimeString: String,
    forecastDayList: List<ForecastDay>,
    isComeForNextTwoHours: Boolean,
    navHostController: NavHostController,
) {

    val context = navHostController.context
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()


    val currentLocalDate = LocalDate.now()


    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val groupedListOfForecastHour by remember(forecastDayList) {
        mutableStateOf(
            forecastDayList.groupBy {
                try {
                    LocalDate.parse(it.date, dateFormatter)
                } catch (e: Exception) {
                    e.printStackTrace()
                    currentLocalDate
                } to it.hour
            }.map { it.key }
        )
    }


    LaunchedEffect(groupedListOfForecastHour) {
        groupedListOfForecastHour.forEach {
            println("Date: ${it.first}")
        }
    }

    LaunchedEffect(groupedListOfForecastHour) {

        println("Forecast Hours: List $forecastDayList")
        println("Forecast Hours: Current Formatted Time String: $currentCityFormattedTimeString")

        if (isComeForNextTwoHours) {

            val currentDateGroupedForecastHour = groupedListOfForecastHour.firstOrNull()

            println("Forecast Hours: currentDateGroupedForecastHour: $currentDateGroupedForecastHour")

            if (currentDateGroupedForecastHour != null) {

                val currentHourIndex = currentDateGroupedForecastHour.second.indexOfFirst {
                    it.isCurrentForecastHour(currentCityFormattedTimeString)
                }

                println("Forecast Hours: Current Forecast Hour Index: $currentHourIndex")

                if (currentHourIndex >= 0 && currentHourIndex <= currentDateGroupedForecastHour.second.size) {

                    lazyListState.scrollToItem(currentHourIndex)
                }

            }

        }

    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "72-hour Forecast",
                        style = TextStyle(
                            fontSize = 18.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navHostController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Icon for Navigate Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackgroundColor,
                    titleContentColor = White,
                    navigationIconContentColor = White
                )
            )
        }
    ) { innerPadding ->

        LazyColumn(
            state = lazyListState,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = DarkBackgroundColor)
        ) {

            if (forecastDayList.isNotEmpty()) {

                groupedListOfForecastHour.forEachIndexed { forecastDayIndex, (forecastDayDate, forecastHoursList) ->

                    val currentForecastHourIndex = forecastHoursList.indexOfFirst {
                        it.isCurrentForecastHour(currentCityFormattedTimeString)
                    }


                    stickyHeader(
                        key = forecastDayDate
                    ) {

                        AnimatedVisibility(
                            visible = lazyListState.firstVisibleItemIndex > 2,
                            enter = slideInVertically {
                                -it
                            },
                            exit = slideOutVertically { -it }
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = DarkBackgroundColor,
                                ),
                                shape = RoundedCornerShape(0.dp),
                                modifier = modifier
                                    .fillMaxWidth()
                            ) {

                                val textToShow = if (
                                    forecastDayDate.dayOfMonth == currentLocalDate.dayOfMonth
                                        && forecastDayDate.month.value == currentLocalDate.month.value
                                    ) "Today"
                                else if(
                                    forecastDayDate.dayOfMonth == currentLocalDate.dayOfMonth + 1
                                    && forecastDayDate.month.value == currentLocalDate.month.value
                                ) "Tomorrow"
                                else forecastDayDate.dayOfWeek.name
                                    .toLowerCase().replaceFirstChar { it.toUpperCase() }

                                Text(
                                    text = textToShow,
                                    color = White,
                                    modifier = modifier
                                        .padding(all = 10.dp)
                                )
                            }
                        }
                    }


                    items(
                        count = forecastHoursList.size,
                        key = {
                            it
                        }
                    ) { forecastHourIndex ->

                        val forecastHour = forecastHoursList[forecastHourIndex]

                        val forecastHourLocalDate by remember(forecastHoursList) {
                            derivedStateOf {
                                forecastDayDate
                            }
                        }

                        ForecastHourListItem(
                            forecastHour = forecastHour,
                            forecastHourLocalDate = forecastHourLocalDate,
                            context = context,
                            isFirstIndex = forecastHourIndex == 0,
                            isLastIndex = forecastHourIndex == forecastHoursList.size,
                            index = forecastHourIndex,
                            currentHourIndex = currentForecastHourIndex,
                            isFirstDayIndex = forecastDayIndex == 0,
                            isComeForNextTwoHours = isComeForNextTwoHours,
                            modifier = modifier,
                        )

                    }

                }
            }

            if (forecastDayList.isEmpty()) {
                item {
                    ProgressIndicator()
                }
            }

        }

    }


}

@Composable
private fun ForecastHourListItem(
    modifier: Modifier = Modifier,
    forecastHour: ForecastHour,
    forecastHourLocalDate: LocalDate,
    context: Context,
    isFirstIndex: Boolean,
    index: Int,
    currentHourIndex: Int,
    isFirstDayIndex: Boolean,
    isComeForNextTwoHours: Boolean,
    isLastIndex: Boolean,
) {

    val hourlyForecastTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")


    val timeFormatter12Hours = DateTimeFormatter.ofPattern("hh:mm a")


    val weekName by remember(forecastHour) {
        derivedStateOf {
            (forecastHourLocalDate.dayOfWeek.name).takeFirstThreeWithFirstUpper()
        }
    }

    val month by remember(forecastHour) {
        derivedStateOf {
            forecastHourLocalDate.month.value
        }
    }
    val day by remember(forecastHour) {
        derivedStateOf {
            forecastHourLocalDate.dayOfMonth
        }
    }


    Card(
        colors = CardDefaults.cardColors(
            containerColor = DarkCardBackgroundColor
        ),
        shape = RoundedCornerShape(
            topStart = if (isFirstIndex) 10.dp else 0.dp,
            topEnd = if (isFirstIndex) 10.dp else 0.dp,
            bottomStart = if (isLastIndex) 10.dp else 0.dp,
            bottomEnd = if (isLastIndex) 10.dp else 0.dp,
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 10.dp,
                end = 10.dp,
                bottom = if (isLastIndex) 10.dp else 0.dp
            )
    ) {
        Column {

            if (isFirstIndex) {
                Text(
                    text = "$weekName $month/$day",
                    style = TextStyle(
                        color = White,
                        fontSize = 12.sp
                    ),
                    modifier = modifier
                        .padding(all = 10.dp)
                )
            }

            val forecastHoursLocalTime: LocalTime? = try {
                LocalTime.parse(forecastHour.time, hourlyForecastTimeFormatter)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            val formattedForecastHoursTimeString = try {
                timeFormatter12Hours.format(forecastHoursLocalTime)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }


            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = modifier
            ) {

                HorizontalDivider(
                    color = Gray,
                    modifier = modifier.fillMaxWidth()
                )

                if (isFirstDayIndex) {
                    val next1HourIndex = currentHourIndex + 1

                    if (isComeForNextTwoHours && (index == currentHourIndex
                                || index == next1HourIndex)
                    ) {

                        val text: String = when (index) {

                            currentHourIndex -> "** Current hour forecast **"

                            next1HourIndex -> "** Next 2 hours forecast **"

                            else -> ""
                        }

                        Text(
                            text = text,
                            style = TextStyle(
                                color = Red
                            ),
                            modifier = modifier
                                .padding(all = 5.dp)
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = modifier
                        .padding(all = 10.dp)
                ) {
                    // Time and Temp
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // Time
                        Text(
                            text = if (index == currentHourIndex && isFirstDayIndex) "Now" else formattedForecastHoursTimeString,
                            style = TextStyle(
                                color = White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            ),
                        )

                        // Forecast Hour Icon
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(forecastHour.condition.icon.getFullUrlOfTempConditionIconFromApi())
                                .crossfade(true)
                                .build(),
                            contentDescription = "Forecast Hour Icon",
                            contentScale = ContentScale.Crop,
                            modifier = modifier
                                .width(50.dp)
                                .height(50.dp),
                        )

                        // Temp
                        Text(
                            text = "${forecastHour.temp_c}${context.getString(R.string.degree_sign)}",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                        )

                    }

                    // Details
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Condition
                        Text(
                            text = forecastHour.condition.text,
                            style = TextStyle(
                                color = White,
                                fontSize = 12.sp
                            ),
                        )

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

                            // Actual Value
                            Text(
                                text = "${forecastHour.feelslike_c}${context.getString(R.string.degree_sign)}",
                                style = TextStyle(
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
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

                            // Actual Value
                            Text(
                                text = "${forecastHour.wind_mph}mph.${forecastHour.wind_dir}",
                                style = TextStyle(
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
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

                            // Actual Value
                            Text(
                                text = "${forecastHour.gust_mph}mph",
                                style = TextStyle(
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
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

                            // Actual Value
                            Text(
                                text = "${forecastHour.precip_in.takeIntIfAfterDecimalZero()}%",
                                style = TextStyle(
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
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
                                text = "${forecastHour.cloud}%",
                                style = TextStyle(
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                        }


                    }

                }

            }

        }


    }
}