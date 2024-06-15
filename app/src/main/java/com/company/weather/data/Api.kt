package com.company.weather.data

import com.company.weather.BuildConfig
import com.company.weather.data.models.CityResponse
import com.company.weather.data.models.WeatherResponse
import com.company.weather.data.models.toCity
import com.company.weather.data.models.toWeather
import com.company.weather.data.util.Resource
import com.company.weather.domain.model.City
import com.company.weather.domain.model.Weather
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parametersOf
import javax.inject.Inject

class Api @Inject constructor(
    private val client: HttpClient,
) {
    private companion object {
        const val CITIES_URL =
            "https://gist.githubusercontent.com/Stronger197/764f9886a1e8392ddcae2521437d5a3b/raw/65164ea1af958c75c81a7f0221bead610590448e/cities.json"
        const val WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather"
    }

    suspend fun getCities(): Resource<List<City>> {
        return try {
            val result = client.get(CITIES_URL).body<List<CityResponse>>()
            if (result.isEmpty()) Resource.Error()
            else Resource.Success(result.filter { it.cityName.isNotEmpty() }.map { it.toCity() })
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }

    suspend fun getWeather(latitude: String, longitude: String): Resource<Weather> {
        return try {
            val result = client.get(WEATHER_URL) {
                parameter("lat", latitude)
                parameter("lon", longitude)
                parameter("units", "metric")
                parametersOf("exclude", listOf("minutely", "hourly", "daily", "alerts"))
                parameter("appid", BuildConfig.OPEN_WEATHER_MAP_KEY)
            }.body<WeatherResponse>().toWeather()
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }
}