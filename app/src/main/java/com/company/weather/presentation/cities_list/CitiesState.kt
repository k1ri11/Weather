package com.company.weather.presentation.cities_list

import com.company.weather.domain.model.City

data class CitiesState(
    val citiesMap: Map<String, List<City>>,
    val startIndexes: List<Int>,
    val endIndexes: List<Int>,
    val sortedCities: List<City>,
)