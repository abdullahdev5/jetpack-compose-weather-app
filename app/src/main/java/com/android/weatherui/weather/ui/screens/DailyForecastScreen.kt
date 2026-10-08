package com.android.weatherui.weather.ui.screens

import android.content.Context
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.android.weatherui.R
import com.android.weatherui.common.domain.extensions.getFullUrlOfTempConditionIconFromApi
import com.android.weatherui.common.domain.extensions.takeFirstThreeWithFirstUpper
import com.android.weatherui.takeIntIfAfterDecimalZero
import com.android.weatherui.ui.theme.DarkBackgroundColor
import com.android.weatherui.ui.theme.DarkCardBackgroundColor
import com.android.weatherui.ui.theme.Gray
import com.android.weatherui.ui.theme.LightBlue
import com.android.weatherui.ui.theme.Red
import com.android.weatherui.ui.theme.White
import com.android.weatherui.weather.domain.constants.InlineTextContentIds
import com.android.weatherui.weather.domain.models.weather_related.ForecastDay
import com.android.weatherui.weather.ui.common_composables.InlineTextContentIcon
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

val WeekDayNames = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DailyForecastScreen(
    modifier: Modifier = Modifier,
    forecastDaysList: List<ForecastDay>,
    navHostController: NavHostController,
    onDetailsClick: (selectedForecastDayIndex: Int?) -> Unit,
) {

    val context = navHostController.context

    var selectedTabIndex by rememberSaveable() {
        mutableIntStateOf(0)
    }


    Scaffold(
        topBar = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = DarkBackgroundColor)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "14-day Forecast",
                            style = TextStyle(
                                fontSize = 18.sp,
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

                // Tab Row
                PrimaryTabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = DarkBackgroundColor,
                    contentColor = White,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            color = White,
                            modifier = modifier
                                .tabIndicatorOffset(selectedTabIndex, matchContentSize = true)
                        )
                    },
                    divider = {}, // '{}' Means Empty (No Divider)
                    modifier = modifier
                        .fillMaxWidth(),
                ) {
                    // Daily
                    Tab(
                        selected = selectedTabIndex == 0,
                        text = {
                            Text(text = "Daily")
                        },
                        onClick = {
                            if (selectedTabIndex != 0) {
                                selectedTabIndex = 0
                            }
                        },
                        selectedContentColor = White,
                        unselectedContentColor = Gray,
                    )

                    // Monthly
                    Tab(
                        selected = selectedTabIndex == 1,
                        text = {
                            Text(text = "Monthly")
                        },
                        onClick = {
                            if (selectedTabIndex != 1) {
                                selectedTabIndex = 1
                            }
                        },
                        selectedContentColor = White,
                        unselectedContentColor = Gray
                    )

                }

                // Extra Space
                Spacer(modifier = modifier.height(10.dp))

            }
        },
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = DarkBackgroundColor)
                .then(
                    if (selectedTabIndex == 1) {
                        modifier.verticalScroll(rememberScrollState())
                    } else modifier
                )
        ) {

            when (selectedTabIndex) {

                0 -> {
                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize()
                    ) {

                        items(
                            count = forecastDaysList.size
                        ) { index ->

                            val forecastDay = forecastDaysList[index]

                            DailyForecastListItem(
                                modifier = modifier,
                                forecastDay = forecastDay,
                                context = context,
                                index = index,
                                onDetailsClick = { clickedForecastDayIndex ->
                                    println("Forecast Day: Clicked Forecast Day: $clickedForecastDayIndex")
                                    onDetailsClick(
                                        clickedForecastDayIndex
                                    )
                                }
                            )

                        }

                    }

                }

                1 -> {


                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = DarkCardBackgroundColor
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp)
                    ) {

                        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                        // Determine the months present in the forecast
                        val monthSet = forecastDaysList.map {
                            LocalDate.parse(it.date, dateFormatter).month
                        }.distinct()

                        // Format the month label
                        val monthLabel = when (monthSet.size) {
                            1 -> monthSet.first()
                                .getDisplayName(
                                    java.time.format.TextStyle.FULL,
                                    Locale.getDefault()
                                ) // Single month
                            2 -> "${
                                monthSet.first().getDisplayName(
                                    java.time.format.TextStyle.FULL,
                                    Locale.getDefault()
                                )
                            } & " +
                                    monthSet.last().getDisplayName(
                                        java.time.format.TextStyle.FULL,
                                        Locale.getDefault()
                                    ) // Two months
                            else -> ""
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                            Column {

                                Text(
                                    text = monthLabel,
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = White
                                    ),
                                    modifier = modifier
                                        .padding(all = 10.dp)
                                )

                                // Week Names
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .padding(all = 10.dp),
                                ) {
                                    WeekDayNames.forEach { weekName ->
                                        Text(
                                            text = weekName,
                                            style = TextStyle(
                                                fontSize = 12.sp,
                                                color = White,
                                                textAlign = TextAlign.Center
                                            ),
                                            modifier = modifier
                                                .weight(1f, fill = true)
                                        )
                                    }
                                }

                                Spacer(modifier = modifier.height(10.dp))

                                HorizontalDivider(
                                    modifier = modifier.fillMaxWidth(),
                                    color = Gray
                                )

                                Spacer(modifier = modifier.height(10.dp))


                                val today = LocalDate.now()

                                val firstForecastDay = remember(forecastDaysList) {
                                    forecastDaysList.first()
                                }

                                val firstForecastDate = try {
                                    LocalDate.parse(
                                        firstForecastDay.date,
                                        dateFormatter
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    today
                                }

                                val firstWeekDayIndex = firstForecastDate.dayOfWeek.value % 7
                                val emptyStartSpaces = firstWeekDayIndex


                                // Create Cells List for Display
                                val cells = mutableListOf<ForecastDay?>().apply {
                                    repeat(emptyStartSpaces) { add(null) }
                                    addAll(forecastDaysList)
                                }

                                val totalDaysToShow = cells.size
                                val totalTableRows = (totalDaysToShow / 7) + if (
                                    totalDaysToShow % 7 > 0
                                ) 1 else 0
                                val remainingSlots = (totalTableRows * 7) + totalDaysToShow
                                repeat(remainingSlots) { cells.add(null) }


                                for (row in 0 until totalTableRows) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = modifier.fillMaxWidth().padding(all = 10.dp)
                                    ) {
                                        for (col in 0 until 7) {
                                            val index = row * 7 + col
                                            val forecastDay = cells.getOrNull(index)

                                            Box(
                                                modifier = Modifier
                                                    .weight(1f, true)
                                                    .padding(4.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (forecastDay == null) {
                                                    Text(
                                                        text = "",
                                                        style = TextStyle(
                                                            fontSize = 15.sp,
                                                        )
                                                    )
                                                } else {
                                                    MonthlyForecastCalendarListItem(
                                                        modifier = Modifier,
                                                        dateFormatter = dateFormatter,
                                                        forecastDay = forecastDay,
                                                        context = context
                                                    )
                                                }
                                            }

                                        }
                                    }
                                }


                            }

                        }

                    }


                }

            }

        }

    }


}


@Composable
private fun DailyForecastListItem(
    modifier: Modifier = Modifier,
    forecastDay: ForecastDay,
    context: Context,
    index: Int,
    onDetailsClick: (Int) -> Unit,
) {

    val nightForecastHoursList = forecastDay.hour.filter { it.is_day == 0 }

    val avgNightTemp = nightForecastHoursList.map { it.temp_c }.average()
    val nightTempConditionText =
        nightForecastHoursList.firstOrNull { it.temp_c.toInt() == avgNightTemp.toInt() }?.condition?.text
            ?: nightForecastHoursList.first().condition.text

    val nightTempConditionIconUrl =
        nightForecastHoursList.firstOrNull { it.temp_c.toInt() == avgNightTemp.toInt() }?.condition?.icon?.getFullUrlOfTempConditionIconFromApi()
            ?: nightForecastHoursList.first().condition.icon.getFullUrlOfTempConditionIconFromApi()


    val dailyForecastDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val localDate = try {
        LocalDate.parse(forecastDay.date, dailyForecastDateTimeFormatter)
    } catch (e: Exception) {
        e.printStackTrace()
        LocalDate.now()
    }

    val month = localDate.month.value
    val day = localDate.dayOfMonth
    val weekName = localDate.dayOfWeek.name.takeFirstThreeWithFirstUpper()



    Card(
        colors = CardDefaults.cardColors(
            containerColor = DarkCardBackgroundColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 10.dp)
            .clickable {
                onDetailsClick(index)
            }
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // Date and Details Text
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(all = 5.dp)
            ) {

                Text(
                    text = "$month/$day $weekName",
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Gray,
                    )
                )

                Spacer(modifier = modifier.weight(1f))

                Text(
                    text = buildAnnotatedString {
                        append("Details")
                        appendInlineContent(
                            id = InlineTextContentIds.SIMPLE_ARROW_RIGHT,
                            alternateText = InlineTextContentIds.SIMPLE_ARROW_RIGHT
                        )
                    },
                    inlineContent = InlineTextContentIcon(
                        id = InlineTextContentIds.SIMPLE_ARROW_RIGHT,
                        iconColor = Gray
                    ),
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Gray,
                    ),
                )
            }

            HorizontalDivider(
                color = Gray,
                modifier = modifier
                    .fillMaxWidth()
            )

            // Day
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = modifier
                    .padding(all = 10.dp),
            ) {

                // Day Text
                Text(
                    text = "Day",
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Gray
                    )
                )

                // Day Icon
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(forecastDay.day.condition.icon.getFullUrlOfTempConditionIconFromApi())
                        .crossfade(true)
                        .build(),
                    contentDescription = "Day Temp Condition Icon",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .width(50.dp)
                        .height(50.dp)
                )

                // Day Temp Condition Text
                Text(
                    text = forecastDay.day.condition.text,
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Gray
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.weight(1f)
                )

                // Day Avg Temp
                Text(
                    text = "${
                        forecastDay.day.avgtemp_c.takeRoundedValue()
                            .takeIntIfAfterDecimalZero()
                    }${
                        context.getString(R.string.degree_sign)
                    }",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Red
                    )
                )

            }

            // Night
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = modifier
                    .padding(all = 10.dp),
            ) {

                // Night Text
                Text(
                    text = "Night",
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Gray
                    )
                )

                // Night Icon
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(nightTempConditionIconUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Night Temp Condition Icon",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .width(50.dp)
                        .height(50.dp)
                )

                // Night Temp Condition Text
                Text(
                    text = nightTempConditionText,
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Gray
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = modifier.weight(1f)
                )

                // Night Temp
                Text(
                    text = "${
                        avgNightTemp.takeRoundedValue()
                            .takeIntIfAfterDecimalZero()
                    }${
                        context.getString(R.string.degree_sign)
                    }",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightBlue
                    )
                )

            }


        }

    }

}


@Composable
fun MonthlyForecastCalendarListItem(
    modifier: Modifier = Modifier,
    forecastDay: ForecastDay,
    dateFormatter: DateTimeFormatter?,
    context: Context,
) {

    val localDate = try {
        LocalDate.parse(
            forecastDay.date,
            dateFormatter
        )
    } catch (e: Exception) {
        e.printStackTrace()
        LocalDate.now()
    }


    val avgNightTemp = forecastDay.hour.filter {
        it.is_day == 0
    }.map {
        it.temp_c
    }.average().takeRoundedValue{ 1 }.takeIntIfAfterDecimalZero()

//    val minTemp = forecastDay.day.mintemp_c.takeIntIfAfterDecimalZero()
    val avgDayTemp = forecastDay.day.avgtemp_c.takeIntIfAfterDecimalZero()


    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(bottom = 10.dp)
    ) {

        Text(
            text = "${localDate.month.value}/${
                localDate.dayOfMonth
            }",
            style = TextStyle(
                fontSize = 15.sp,
                color = White
            )
        )

        // Temperature Condition Icon
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(forecastDay.day.condition.icon.getFullUrlOfTempConditionIconFromApi())
                .crossfade(true)
                .build(),
            contentDescription = "Temperature Condition Icon",
            contentScale = ContentScale.Fit,
            modifier = modifier
                .widthIn(40.dp)
                .heightIn(40.dp)
        )

        // Average Night Temperature
        Text(
            text = "$avgNightTemp${
                context.getString(R.string.degree_sign)
            }",
            style = TextStyle(
                fontSize = 14.sp,
                color = White,
                fontWeight = FontWeight.Bold,
            )
        )

        // Average Day Temperature
        Text(
            text = "$avgDayTemp${
                context.getString(R.string.degree_sign)
            }",
            style = TextStyle(
                fontSize = 14.sp,
                color = White,
                fontWeight = FontWeight.Bold,
            )
        )

    }

}
