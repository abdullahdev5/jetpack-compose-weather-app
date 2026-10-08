package com.android.weatherui.weather.ui.screens

import androidx.compose.foundation.Image
import com.android.weatherui.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.android.weatherui.ui.theme.DarkBackgroundColor
import com.android.weatherui.ui.theme.DarkCardBackgroundColor
import com.android.weatherui.ui.theme.SimpleCardBgColor
import com.android.weatherui.ui.theme.White
import com.android.weatherui.weather.domain.constants.AirQualityRelated

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AirPollutionLevelScreen(
    airQualityLevel: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val currentAirQualityLevelIconResId =
        AirQualityRelated.getAirQualityLevelIconResId(airQualityLevel)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Air Pollution Level",
                        style = TextStyle(
                            fontSize = 18.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
        },
        bottomBar = {
            BottomAppBar(
                containerColor = DarkBackgroundColor,
                modifier = modifier
                    .fillMaxWidth(),
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .background(color = DarkBackgroundColor)
                ) {
                    val airQualityLevelEmojiIconsList = listOf(
                        R.drawable.air_quality_sensitive_icon,
                        R.drawable.air_quality_unhealthy_icon,
                        R.drawable.air_quality_hazardous_icon,
                        R.drawable.air_quality_very_unhealthy_icon,
                        R.drawable.air_quality_moderate_icon,
                        R.drawable.air_quality_good_icon,
                    )

                    airQualityLevelEmojiIconsList
                        .filter {
                            it != currentAirQualityLevelIconResId
                        }
                        .forEach { iconResId ->

                            // Air Quality Level Emojis Icons
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Image(
                                    painter = painterResource(iconResId),
                                    contentDescription = "Air Quality Level Emojis Icon",
                                    contentScale = ContentScale.Crop,
                                    modifier = modifier
                                        .padding(all = 5.dp)
                                        .width(40.dp)
                                        .then(
                                            if (iconResId == R.drawable.air_quality_hazardous_icon)
                                                modifier.height(50.dp)
                                            else modifier.height(40.dp)
                                        )
                                )
                            }

                        }
                }
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
                .verticalScroll(rememberScrollState())
        ) {

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = DarkCardBackgroundColor
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = modifier
                        .padding(all = 10.dp)
                ) {
                    // Emoji & AirQuality Range
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = SimpleCardBgColor,
                            ),
                            shape = RoundedCornerShape(5.dp)
                        ) {

                            // Emoji
                            Image(
                                painter = painterResource(currentAirQualityLevelIconResId),
                                contentDescription = "Air Quality Level Emoji Icon",
//                                tint = Yellow,
                                modifier = modifier
                                    .width(100.dp)
                                    .height(100.dp)
                                    .padding(all = 10.dp)
                            )

                        }

                        val airQualityRange =
                            AirQualityRelated.getAirQualityLevelRangeFromLevel(airQualityLevel)
                        // Range
                        Text(
                            text = airQualityRange,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            ),
                            modifier = modifier
                                .align(Alignment.CenterHorizontally)
                        )

                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {

                        val airQualityLevelText =
                            AirQualityRelated.getAirQualityLevel(airQualityLevel)
                        val airQualityLevelDescription =
                            AirQualityRelated.getAirQualityLevelDescription(airQualityLevel)
                        val airQualityColor =
                            AirQualityRelated.getAirQualityLevelColor(airQualityLevel)

                        Card(
                            shape = RoundedCornerShape(5.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = airQualityColor,
                                contentColor = White
                            )
                        ) {
                            // Air Quality Level
                            Text(
                                text = airQualityLevelText,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color = White,
                                ),
                                modifier = modifier
                                    .padding(all = 5.dp)
                            )
                        }

                        // Air Quality Level Description
                        Text(
                            text = airQualityLevelDescription,
                            style = TextStyle(
                                fontSize = 10.sp,
                                color = White
                            )
                        )

                    }

                }
            }

        }

    }
}