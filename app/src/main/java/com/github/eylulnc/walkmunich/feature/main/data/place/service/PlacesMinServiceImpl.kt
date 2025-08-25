package com.github.eylulnc.walkmunich.feature.main.data.place.service

import android.content.Context
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.data.model.PlacesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class PlacesMinServiceImpl(
    private val context: Context,
    private val json: Json
) : PlacesMinService {

    override suspend fun fetchPlaces(): List<Place> = withContext(Dispatchers.IO) {
        delay(300)
        // In CityServiceImpl and PlacesMinServiceImpl before opening the file:
        val files = context.assets.list("api")?.toList() ?: emptyList()
        check("place.min.json" in files) { "assets/api/place.min.json missing. Found: $files" }

        val path = "place.min.json"
        val text = context.assets.open(path).bufferedReader().use { it.readText() }
        json.decodeFromString(PlacesResponse.serializer(), text).places
    }
}