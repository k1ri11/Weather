package com.company.weather

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.company.weather.presentation.cities_list.CitiesListScreen
import com.company.weather.presentation.weather.WeatherScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ScreenCities
    ) {
        composable<ScreenCities> {
            CitiesListScreen { city ->
                navController.navigate(
                    ScreenWeather(
                        latitude = city.latitude,
                        longitude = city.longitude,
                        cityName = city.cityName,
                    )
                )
            }
        }
        composable<ScreenWeather> {
            val args = it.toRoute<ScreenWeather>()
            WeatherScreen(
                latitude = args.latitude,
                longitude = args.longitude,
                cityName = args.cityName,
            )

        }
    }
}

@Serializable
object ScreenCities

@Serializable
data class ScreenWeather(
    val latitude: String,
    val longitude: String,
    val cityName: String,
)