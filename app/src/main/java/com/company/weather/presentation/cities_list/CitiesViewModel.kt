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

    private val _citiesState = MutableStateFlow<ScreenState<List<City>>>(ScreenState.Loading())
    val citiesState: StateFlow<ScreenState<List<City>>> = _citiesState.asStateFlow()
    fun getCities() = viewModelScope.launch(dispatchers.default) {
        _citiesState.value = ScreenState.Loading()
        repository.getCities().collect { result ->
            when (result) {
                is Resource.Loading -> {
                    _citiesState.value = ScreenState.Loading()
                }

                is Resource.Success -> {
                    val sortedList = result.data!!.sortedBy { it.cityName }
                    _citiesState.value = ScreenState.Success(sortedList)
                }

                is Resource.Error -> {
                    _citiesState.value = ScreenState.Error(context.getString(R.string.error_message))
                }
            }
        }
    }
}
