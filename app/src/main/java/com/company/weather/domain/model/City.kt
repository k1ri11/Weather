package com.company.weather.domain.model

data class City(
    val cityName: String,
    val firstChar: Char,
    val id: String,
    val latitude: String,
    val longitude: String,
)