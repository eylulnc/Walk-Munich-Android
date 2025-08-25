package com.github.eylulnc.walkmunich.feature.main.data.place.service

import android.content.Context
import com.github.eylulnc.walkmunich.core.data.model.PlaceMin
import com.github.eylulnc.walkmunich.core.data.model.PlacesMinResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class PlacesMinServiceImpl(
    private val context: Context,
    private val json: Json
) : PlacesMinService {

    override suspend fun fetchPlaces(): List<PlaceMin> = withContext(Dispatchers.IO) {
        val text = context.assets.open("api/places.min.json").bufferedReader().use { it.readText() }
        json.decodeFromString(
            PlacesMinResponse
                .serializer(), text
        ).places
    }
}