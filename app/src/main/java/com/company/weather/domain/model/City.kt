package com.company.weather.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class City(
    @SerialName("city")
    val cityName: String,
    @SerialName("id")
    val id: String,
    @SerialName("latitude")
    val latitude: String,
    @SerialName("longitude")
    val longitude: String,
)