package com.github.eylulnc.walkmunich.feature.main.data

import com.github.eylulnc.walkmunich.core.data.model.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CityRepository(
    private val service: CityService
) {
    fun getCity(): Flow<City> = flow {
        val city = service.fetchCity()
        emit(city)
    }
}