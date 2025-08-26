package com.github.eylulnc.walkmunich.feature.main.data.place.service

import com.github.eylulnc.walkmunich.core.data.model.Place

interface PlacesService {
    suspend fun fetchPlaces(): List<Place>
}