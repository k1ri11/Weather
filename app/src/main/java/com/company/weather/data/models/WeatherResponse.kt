package com.company.weather.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("main")
    val temp: Temperature,
    @SerialName("name")
    val name: String,
)

@Serializable
data class Temperature(
    @SerialName("temp")
    val temperature: Double,
)