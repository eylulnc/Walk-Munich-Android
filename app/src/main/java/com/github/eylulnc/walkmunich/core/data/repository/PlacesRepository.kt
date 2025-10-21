package com.github.eylulnc.walkmunich.core.data.repository

import com.github.eylulnc.walkmunich.core.data.model.Category
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.data.service.PlacesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlacesRepository(
    private val service: PlacesService
) {
    suspend fun getPlaces(): List<Place> = service.fetchPlaces()
    suspend fun getPlace(id:Long): Place = service.getPlace(id)

    suspend fun getPlaces(category: Category): List<Place> = withContext(Dispatchers.IO) {
        getPlaces().filter { it.category == category }
    }
}