package com.company.weather.data.models


import com.company.weather.domain.model.Weather
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

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

fun WeatherResponse.toWeather() = Weather(
    id = id,
    temperature = temp.temperature.roundToInt().toString().plus("°C"),
    name = name,
)