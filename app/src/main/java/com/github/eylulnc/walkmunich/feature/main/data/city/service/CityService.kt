package com.github.eylulnc.walkmunich.feature.main.data.city.service

import com.github.eylulnc.walkmunich.core.data.model.City

interface CityService {
    suspend fun fetchCity(): City
}
