package com.github.eylulnc.walkmunich.core.data.repository

import com.github.eylulnc.walkmunich.core.data.model.Category
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.data.service.PlacesService

class PlacesRepository(
    private val service: PlacesService
) {
    suspend fun getPlaces(): List<Place> = service.fetchPlaces()
    suspend fun getPlace(id:Long): Place = service.getPlace(id)

    suspend fun getPlace(category: Category): Place = service.getPlace(category)
}