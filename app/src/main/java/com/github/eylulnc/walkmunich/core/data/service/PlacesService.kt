package com.github.eylulnc.walkmunich.core.data.service

import com.github.eylulnc.walkmunich.core.data.model.Place

interface PlacesService {
    suspend fun fetchPlaces(): List<Place>
}