package com.android.weatherui.weather.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.weatherui.ui.theme.Black
import com.android.weatherui.ui.theme.DarkBackgroundColor
import com.android.weatherui.ui.theme.DarkCardBackgroundColor
import com.android.weatherui.ui.theme.Gray
import com.android.weatherui.ui.theme.White
import com.android.weatherui.weather.domain.constants.FamousCities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "CitySelectionScreen.kt"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelectionScreen(
    modifier: Modifier,
    previousSelectedCity: String,
    onQueryChange: (query: String) -> Unit,
    filteredCitiesList: List<String>,
    onSelect: (cityNane: String) -> Unit,
    onBack: () -> Unit,
) {

    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            false
        }
    )


    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current


    var query by rememberSaveable { mutableStateOf("") }

    var isSearchFieldVisible by rememberSaveable {
        mutableStateOf(false)
    }


    LaunchedEffect(key1 = isSearchFieldVisible) {
        if (isSearchFieldVisible) {
            focusRequester.requestFocus()
        } else {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }


    Scaffold(
        containerColor = DarkBackgroundColor,
        modifier = modifier
            .fillMaxSize()
            .background(color = DarkBackgroundColor)
    ) { innerPadding ->

//        Column(
//            modifier = modifier
//
//        ) {

//        if (isCitySelectionSheetStateVisible) {

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch(Dispatchers.Default) {
                    sheetState.hide()
                }

                onBack.invoke()
            },
            properties = ModalBottomSheetDefaults.properties(
                shouldDismissOnBackPress = true,
            ),
            containerColor = DarkBackgroundColor,
            dragHandle = {
                BottomSheetDefaults.DragHandle(
                    color = White
                )
            },
            modifier = modifier
                .fillMaxSize()
                .background(color = DarkCardBackgroundColor)
                .padding(innerPadding)
        ) {
            CitySelectionBottomSheet(
                modifier = modifier,
                previousSelectedCity = previousSelectedCity,
                query = { query },
                onQueryChange = { thisQuery ->
                    query = thisQuery
                    onQueryChange(query)
                },
                onSelect = { city ->
                    onSelect(city)
                },
                focusRequester = focusRequester,
                onSearchFieldVisible = { isSearchFieldVisible = true },
                isSearchFieldVisible = { isSearchFieldVisible },
                filteredCitiesList = filteredCitiesList,
                onBack = {
                    keyboardController?.hide()
                    isSearchFieldVisible = false
                },
                onNavigateBack = onBack
            )

        }

//        }

//        }

    }


}

@Composable
fun CitySelectionBottomSheet(
    modifier: Modifier = Modifier,
    previousSelectedCity: String,
    filteredCitiesList: List<String>,
    query: () -> String,
    onQueryChange: (query: String) -> Unit,
    focusRequester: FocusRequester,
    onSearchFieldVisible: () -> Unit,
    isSearchFieldVisible: () -> Boolean,
    onSelect: (city: String) -> Unit,
    onBack: () -> Unit,
    onNavigateBack: () -> Unit,
) {

    Column {

        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(all = 10.dp)
        ) {
            Text(
                text = "Select City",
                style = TextStyle(
                    color = White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = modifier
                    .align(Alignment.Center)
            )

            IconButton(
                onClick = onNavigateBack,
                modifier = modifier
                    .align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Icon for Navigate Back",
                    tint = White
                )
            }
        }

        AnimatedContent(
            targetState = isSearchFieldVisible(),
            label = ""
        ) { targetState ->

            if (targetState) {
                TextField(
                    value = query(),
                    onValueChange = {
                        onQueryChange(it)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = White,
                        unfocusedIndicatorColor = White,
                        cursorColor = White,
                        focusedTextColor = White,
                        unfocusedTextColor = White,
                    ),
                    placeholder = {
                        Text(
                            text = "Search city...",
                            style = TextStyle(
                                color = White,
                                fontSize = 18.sp,
                                fontStyle = FontStyle.Italic
                            )
                        )
                    },
                    leadingIcon = {
                        IconButton(onClick = {
                            onBack.invoke()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Icon For Navigate Back",
                                tint = White
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search,
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onBack.invoke()
                        }
                    ),
                    singleLine = true,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp)
                        .focusRequester(focusRequester)
                )
            } else {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp)
                        .background(
                            color = DarkCardBackgroundColor,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .clickable {
                            onSearchFieldVisible.invoke()
                        }
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = modifier
                            .align(Alignment.CenterStart)
                            .padding(all = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = White
                        )
                        Text(
                            text = query().ifEmpty { "Search..." },
                            style = TextStyle(
                                color = White
                            )
                        )
                    }

                }
            }

        }


        val selectedCity = FamousCities.firstOrNull {
            it == previousSelectedCity
        }

        Log.d(TAG, "CitySelectionBottomSheet: Previous Selected City: $previousSelectedCity")
        Log.d(TAG, "CitySelectionBottomSheet: Selected City: $selectedCity")

        AnimatedVisibility(!isSearchFieldVisible() && query().isEmpty()) {

            Column {

                Text(
                    text = "Top cities over the world",
                    style = TextStyle(
                        color = Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    modifier = modifier
                        .padding(all = 10.dp)
                )

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(3),
                    userScrollEnabled = true,
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    items(
                        FamousCities.sortedByDescending {
                            it == previousSelectedCity
                        }
                    ) { city ->

                        val isSelectedCity by remember {
                            derivedStateOf {
                                city == previousSelectedCity
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelectedCity && selectedCity != null)
                                    White else DarkCardBackgroundColor,
                                contentColor = if (isSelectedCity && selectedCity != null)
                                    Black else White
                            ),
                            modifier = modifier
                                .padding(all = 5.dp)
                                .clickable {
                                    onSelect(city)
                                }
                        ) {
                            Text(
                                text = city,
                                modifier = modifier
                                    .padding(all = 10.dp)
                            )
                        }
                    }

                }

            }

        }


        if (filteredCitiesList.isNotEmpty()) {

            LazyColumn(
                contentPadding = PaddingValues(all = 10.dp),
                modifier = modifier
                    .fillMaxSize()
            ) {


                items(filteredCitiesList.size) { index ->

                    val cityItem = filteredCitiesList[index]

                    val cityName = cityItem.substringBefore(",")
                    val countryName = cityItem.substringAfter(", ")


                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .animateItem(
                                fadeInSpec = tween(
                                    200,
                                    easing = LinearEasing
                                ),
                                fadeOutSpec = tween(
                                    200,
                                    easing = LinearEasing
                                )
                            )
                            .clickable {
                                onSelect(cityName)
                            }
                    ) {
                        Text(
                            text = "$cityName, $countryName",
                            style = TextStyle(
                                color = White,
                                fontSize = 15.sp
                            ),
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                    top = 30.dp,
                                    bottom = 0.dp
                                )
                        )

                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Gray,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                    top = 5.dp,
                                    bottom = 0.dp
                                )
                        )
                    }


                }

            }

        }

        if (query().isNotEmpty() && filteredCitiesList.isEmpty()) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
            ) {


                Text(
                    text = "No result!",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = White,
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )

            }

        }


    }


}