package com.company.weather.data

import com.company.weather.data.util.Resource
import com.company.weather.domain.model.City
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class Api @Inject constructor(
    private val client: HttpClient,
) {
    private companion object {
        const val CITIES_URL =
            "https://gist.githubusercontent.com/Stronger197/764f9886a1e8392ddcae2521437d5a3b/raw/65164ea1af958c75c81a7f0221bead610590448e/cities.json"
    }

    suspend fun getCities(): Resource<List<City>> {
        return try {
            val result = client.get(CITIES_URL).body<List<City>>()
            if (result.isEmpty()) Resource.Error()
            else Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }
}