package com.android.weatherui.weather.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.weatherui.weather.domain.constants.CitiesList
import com.android.weatherui.weather.domain.constants.CityData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class CitySelectionViewModel: ViewModel() {


    private val _citiesList = MutableStateFlow(CitiesList)
    val citiesList = _citiesList.asStateFlow()


    private val _searchQuery = MutableStateFlow("")
    var searchQuery = _searchQuery.asStateFlow()


    val filteredCitiesList = _searchQuery
//        .debounce(1000L)
        .onEach {  }
        .combine(_citiesList) { query, citiesList ->
            if (query.isEmpty()) {
                emptyList()
            } else {
                citiesList.filter { city ->
                    city.contains(_searchQuery.value, ignoreCase = true)
                }.sortedBy { it }
            }
        }
        .onEach {  }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _citiesList.value
        )



    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }


}