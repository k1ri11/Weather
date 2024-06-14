package com.company.weather.presentation.common

sealed class ScreenState<T> {
    class Loading<T> : ScreenState<T>()
    data class Success<T>(val data: T) : ScreenState<T>()
    data class Error<T>(val message: String) : ScreenState<T>()
}