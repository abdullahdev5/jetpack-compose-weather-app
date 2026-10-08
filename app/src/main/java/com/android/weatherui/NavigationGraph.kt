package com.android.weatherui

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.android.weatherui.weather.ui.screens.AirPollutionLevelScreen
import com.android.weatherui.weather.ui.screens.CitySelectionScreen
import com.android.weatherui.weather.ui.screens.DailyDetailsScreen
import com.android.weatherui.weather.ui.screens.DailyForecastScreen
import com.android.weatherui.weather.ui.screens.HomeScreen
import com.android.weatherui.weather.ui.screens.HourlyForecastScreen
import com.android.weatherui.weather.ui.viewmodels.CitySelectionViewModel
import com.android.weatherui.weather.ui.viewmodels.WeatherViewModel
import org.koin.androidx.compose.koinViewModel


private const val TAG = "NavigationGraph.kt"

private const val animationDuration: Int = 1000


@Composable
fun NavigationGraph(
    navHostController: NavHostController,
    weatherViewModel: WeatherViewModel,
) {



    val weatherModel by weatherViewModel.weatherModel.collectAsStateWithLifecycle()



    NavHost(
        navController = navHostController,
        startDestination = ScreenDestinations.HOME_SCREEN_DESTINATION
    ) {

        // Home Screen
        composable<ScreenDestinations.HOME_SCREEN_DESTINATION>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(animationDuration)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(animationDuration)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(animationDuration)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(animationDuration)
                )
            },
        ) {

            val currentWeather by remember() {
                derivedStateOf {
                    weatherModel?.current
                }
            }

            val currentWeatherLocation by remember() {
                derivedStateOf {
                    weatherModel?.location
                }
            }

            val currentDayForecast by remember() {
                derivedStateOf {
                    weatherModel?.forecast?.forecastDay?.first()
                }
            }

            val forecastDays by remember() {
                derivedStateOf {
                    weatherModel?.forecast?.forecastDay ?: emptyList()
                }
            }


            val currentWeatherAirQuality by remember() {
                derivedStateOf {
                    weatherModel?.current?.airQuality
                }
            }


            val selectedCity = remember {
                it.savedStateHandle.get<String>("city")
            }

            val isUserSelectCityFromSearch by remember() {
                derivedStateOf {
                    (selectedCity ?: "").isNotEmpty()
                }
            }

            val context = LocalContext.current

            LaunchedEffect(selectedCity) {

                Log.d(TAG, "NavigationGraph: Selected City: $selectedCity")

                try {
                    if ((selectedCity ?: "") != (currentWeatherLocation?.city ?: "")) {

                        Log.d(
                            TAG,
                            "NavigationGraph: Selected City is Not Equals to Current Location City: ${currentWeatherLocation?.city ?: ""}"
                        )

                        if (!selectedCity.isNullOrEmpty()) {
                            weatherViewModel.getCurrentWeather(
                                city = selectedCity
                            )
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }


            HomeScreen(
                currentWeather = currentWeather,
                currentWeatherLocation = currentWeatherLocation,
                currentAirQuality = currentWeatherAirQuality,
                forecastDaysList = forecastDays,
                currentDayForecast = currentDayForecast,
                weatherViewModel = weatherViewModel,
                modifier = Modifier,
                onHourlyForecastMoreClick = { currentFormattedCityTime: String, isGoingForNextTwoHours: Boolean ->

                    navHostController.navigate(
                        ScreenDestinations.HourlyForecastScreenDestination(
                            currentCityFormattedTimeString = currentFormattedCityTime,
                            isGoingForNextTwoHours = isGoingForNextTwoHours
                        )
                    ) {
                        launchSingleTop = true
                    }

                },
                onDailyForecastMoreClick = {
                    navHostController.navigate(
                        ScreenDestinations.DAILY_FORECAST_SCREEN_DESTINATION
                    ) {
                        launchSingleTop = true
                    }
                },
                onDetailsClick = {
                    navHostController.navigate(
                        ScreenDestinations.DailyDetailsScreenDestination(
                            selectedForecastDayIndex = null
                        )
                    ) {
                        launchSingleTop = true
                    }
                },
                onAirQualityMoreClick = { airQualityLevel: Int ->
                    navHostController.navigate(
                        ScreenDestinations.AirPollutionLevelScreenDestination(
                            airQualityLevel = airQualityLevel
                        )
                    ) {
                        launchSingleTop = true
                    }
                },
                onCitySelectionClick = {
                    navHostController.navigate(
                        ScreenDestinations.CitySelectionScreenDestination(
                            previousSelectedCity = currentWeatherLocation?.city ?: ""
                        )
                    ) {
                        launchSingleTop = true
                    }
                },
                isUserSelectCityFromSearch = { isUserSelectCityFromSearch }
            )


        }


        // Hourly Forecast Screen
        composable<ScreenDestinations.HourlyForecastScreenDestination>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(animationDuration)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(animationDuration)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(animationDuration)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(animationDuration)
                )
            },
        ) {

            val arguments = it.toRoute<ScreenDestinations.HourlyForecastScreenDestination>()


            val currentCityFormattedTimeString by remember {
                derivedStateOf {
                    arguments.currentCityFormattedTimeString
                }
            }

            val forecastDays by remember() {
                derivedStateOf {
                    weatherModel?.forecast?.forecastDay?.take(3) ?: emptyList()
                }
            }

            HourlyForecastScreen(
                forecastDayList = forecastDays,
                currentCityFormattedTimeString = currentCityFormattedTimeString,
                isComeForNextTwoHours = arguments.isGoingForNextTwoHours,
                navHostController = navHostController,
                modifier = Modifier,
            )

        }


        // Daily Forecast Screen
        composable<ScreenDestinations.DAILY_FORECAST_SCREEN_DESTINATION>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(animationDuration)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(animationDuration)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(animationDuration)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(animationDuration)
                )
            },
        ) {

            val fullForecastDaysList by weatherViewModel.fullForecastDaysList.collectAsStateWithLifecycle()

            DailyForecastScreen(
                modifier = Modifier,
                forecastDaysList = fullForecastDaysList,
                navHostController = navHostController,
                onDetailsClick = { selectedForecastDayIndex ->
                    navHostController.navigate(
                        ScreenDestinations.DailyDetailsScreenDestination(
                            selectedForecastDayIndex = selectedForecastDayIndex
                        )
                    ) {
                        launchSingleTop = true
                    }
                }
            )

        }

        composable<ScreenDestinations.AirPollutionLevelScreenDestination>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(animationDuration)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(animationDuration)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(animationDuration)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(animationDuration)
                )
            },
        ) {

            val arguments = it.toRoute<ScreenDestinations.AirPollutionLevelScreenDestination>()

            val currentWeatherAirQualityLevel by remember() {
                derivedStateOf {
                    arguments.airQualityLevel
                }
            }

            AirPollutionLevelScreen(
                modifier = Modifier,
                airQualityLevel = currentWeatherAirQualityLevel,
                onBack = {
                    navHostController.navigateUp()
                }
            )

        }

        composable<ScreenDestinations.DailyDetailsScreenDestination>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(animationDuration)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(animationDuration)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(animationDuration)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(animationDuration)
                )
            },
        ) {

            val arguments = it.toRoute<ScreenDestinations.DailyDetailsScreenDestination>()


            val selectedForecastDayIndex by remember {
                derivedStateOf {
                    arguments.selectedForecastDayIndex
                }
            }


            val fullForecastDaysList by weatherViewModel.fullForecastDaysList.collectAsStateWithLifecycle()


            println("Forecast Day: Arguments Forecast Day: $selectedForecastDayIndex")

            DailyDetailsScreen(
                modifier = Modifier,
                forecastDaysList = fullForecastDaysList,
                selectedForecastDayIndex = selectedForecastDayIndex,
                navHostController = navHostController,
            )

        }


        composable<ScreenDestinations.CitySelectionScreenDestination>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(animationDuration)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(animationDuration)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(animationDuration)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(animationDuration)
                )
            },
        ) {

            val arguments = it.toRoute<ScreenDestinations.CitySelectionScreenDestination>()

            val citySelectionViewModel = koinViewModel<CitySelectionViewModel>()

            val filteredCitiesList by citySelectionViewModel.filteredCitiesList.collectAsStateWithLifecycle()



            CitySelectionScreen(
                filteredCitiesList = filteredCitiesList,
                previousSelectedCity = arguments.previousSelectedCity,
                onQueryChange = { query ->
                    citySelectionViewModel.onSearchQueryChange(query)
                },
                onSelect = { city ->

                    navHostController.previousBackStackEntry!!.savedStateHandle["city"] = city

                    navHostController.navigateUp()

                },
                onBack = {
                    navHostController.navigateUp()
                },
                modifier = Modifier,
            )

        }

    }


}