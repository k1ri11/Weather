package com.company.weather.presentation.weather

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.weather.R
import com.company.weather.data.Repository
import com.company.weather.data.util.Resource
import com.company.weather.di.AppDispatchers
import com.company.weather.domain.model.Weather
import com.company.weather.presentation.common.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: Repository,
    private val dispatchers: AppDispatchers,
    @ApplicationContext
    private val context: Context,
) : ViewModel() {
    private val _weatherState = MutableStateFlow<ScreenState<Weather>>(ScreenState.Loading())
    val weatherState: StateFlow<ScreenState<Weather>> = _weatherState.asStateFlow()

    fun getWeather(latitude: String, longitude: String) = viewModelScope.launch(dispatchers.default) {
        _weatherState.value = ScreenState.Loading()
        repository.getWeather(latitude = latitude, longitude = longitude).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    _weatherState.value = ScreenState.Loading()
                }

                is Resource.Success -> {
                    _weatherState.value = ScreenState.Success(result.data!!)
                }

                is Resource.Error -> {
                    _weatherState.value = ScreenState.Error(context.getString(R.string.error_message))
                }
            }
        }
    }

}