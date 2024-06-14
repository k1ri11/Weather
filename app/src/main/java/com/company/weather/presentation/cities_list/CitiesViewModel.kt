package com.company.weather.presentation.cities_list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.weather.R
import com.company.weather.data.Repository
import com.company.weather.data.util.Resource
import com.company.weather.di.AppDispatchers
import com.company.weather.domain.model.City
import com.company.weather.presentation.common.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitiesViewModel @Inject constructor(
    private val repository: Repository,
    private val dispatchers: AppDispatchers,
    @ApplicationContext
    private val context: Context,
) : ViewModel() {

    private val _citiesState = MutableStateFlow<ScreenState<CitiesState>>(ScreenState.Loading())
    val citiesState: StateFlow<ScreenState<CitiesState>> = _citiesState.asStateFlow()
    fun getCities() = viewModelScope.launch(dispatchers.default) {
        _citiesState.value = ScreenState.Loading()
        repository.getCities().collect { result ->
            val sortedList = result.data!!.filter { it.cityName.isNotEmpty() }.sortedBy { it.cityName }
            val citiesMap = sortedList.groupBy { it.cityName.first().toString() }
            when (result) {
                is Resource.Loading -> {
                    _citiesState.value = ScreenState.Loading()
                }

                is Resource.Success -> {
                    _citiesState.value = ScreenState.Success(
                        CitiesState(
                            citiesMap = citiesMap,
                            startIndexes = getStartIndexes(citiesMap.entries),
                            endIndexes = getEndIndexes(citiesMap.entries),
                            sortedCities = sortedList
                        )
                    )
                }

                is Resource.Error -> {
                    _citiesState.value = ScreenState.Error(context.getString(R.string.error_message))
                }
            }
        }
    }

    private fun getStartIndexes(entries: Set<Map.Entry<String, List<City>>>): List<Int> {
        var acc = 0
        val list = mutableListOf<Int>()
        entries.forEach { entry ->
            list.add(acc)
            acc += entry.value.size
        }
        return list
    }

    private fun getEndIndexes(entries: Set<Map.Entry<String, List<City>>>): List<Int> {
        var acc = 0
        val list = mutableListOf<Int>()
        entries.forEach { entry ->
            acc += entry.value.size
            list.add(acc)
        }
        return list
    }
}

