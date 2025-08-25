package com.github.eylulnc.walkmunich.feature.main.data.place.service

import com.github.eylulnc.walkmunich.core.data.model.Place

interface PlacesMinService {
    suspend fun fetchPlaces(): List<Place>
}