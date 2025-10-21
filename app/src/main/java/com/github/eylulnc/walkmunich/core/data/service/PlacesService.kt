package com.github.eylulnc.walkmunich.core.data.service

import com.github.eylulnc.walkmunich.core.data.model.Category
import com.github.eylulnc.walkmunich.core.data.model.Place

interface PlacesService {
    suspend fun fetchPlaces(): List<Place>
    suspend fun getPlace(id: Long): Place
    suspend fun getPlace(category: Category): Place
}
