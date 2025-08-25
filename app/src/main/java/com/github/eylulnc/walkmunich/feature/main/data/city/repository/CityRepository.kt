package com.github.eylulnc.walkmunich.feature.main.data.city.repository

import com.github.eylulnc.walkmunich.core.data.model.City
import com.github.eylulnc.walkmunich.feature.main.data.city.service.CityService
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