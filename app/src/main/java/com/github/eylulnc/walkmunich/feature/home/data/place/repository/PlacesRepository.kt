package com.github.eylulnc.walkmunich.feature.home.data.place.repository

import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.feature.home.data.place.service.PlacesService

class PlacesRepository(
    private val service: PlacesService
) {
    suspend fun getPlaces(): List<Place> = service.fetchPlaces()
}
