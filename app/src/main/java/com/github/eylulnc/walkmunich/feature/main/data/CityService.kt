package com.github.eylulnc.walkmunich.feature.main.data

import com.github.eylulnc.walkmunich.core.data.model.City

interface CityService {
    suspend fun fetchCity(): City
}
