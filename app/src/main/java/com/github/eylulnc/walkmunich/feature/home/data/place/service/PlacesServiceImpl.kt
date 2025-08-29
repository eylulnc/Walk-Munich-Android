package com.github.eylulnc.walkmunich.feature.home.data.place.service

import android.content.Context
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.data.model.PlacesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class PlacesServiceImpl(
    private val context: Context,
    private val json: Json
) : PlacesService {

    override suspend fun fetchPlaces(): List<Place> = withContext(Dispatchers.IO) {
        delay(300)
        val path = "api/place.min.json"
        val text = context.assets.open(path).bufferedReader().use { it.readText() }
        json.decodeFromString(PlacesResponse.serializer(), text).places
    }
}