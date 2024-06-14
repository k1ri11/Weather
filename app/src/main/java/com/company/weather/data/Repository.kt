package com.company.weather.data

import com.company.weather.data.util.Resource
import com.company.weather.di.AppDispatchers
import com.company.weather.domain.model.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: Api,
    private val dispatchers: AppDispatchers,
) {
    suspend fun getCities(): Flow<Resource<List<City>>> {
        return flow { emit(api.getCities()) }.flowOn(dispatchers.io)
    }
}