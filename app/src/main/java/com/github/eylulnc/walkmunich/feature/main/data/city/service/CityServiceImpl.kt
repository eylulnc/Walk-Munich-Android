package com.github.eylulnc.walkmunich.feature.main.data.city.service

import android.content.Context
import com.github.eylulnc.walkmunich.core.data.model.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class CityServiceImpl(
    private val context: Context,
    private val json: Json
) : CityService {

    override suspend fun fetchCity(): City = withContext(Dispatchers.IO) {
        delay(300) // simulate latency
        val files = context.assets.list("api")?.toList() ?: emptyList()
        check("city.json" in files) { "assets/api/city.json missing. Found: $files" }
        val path = "city.json"
        val text = context.assets.open(path).bufferedReader().use { it.readText() }
        json.decodeFromString(City.serializer(), text)
    }
}
