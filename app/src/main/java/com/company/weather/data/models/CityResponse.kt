package com.company.weather.data.models

import com.company.weather.domain.model.City
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CityResponse(
    @SerialName("city")
    val cityName: String,
    @SerialName("id")
    val id: String,
    @SerialName("latitude")
    val latitude: String,
    @SerialName("longitude")
    val longitude: String,
)

fun CityResponse.toCity() = City(
    cityName = cityName,
    firstChar = cityName.first(),
    id = id,
    latitude = latitude,
    longitude = longitude,
)