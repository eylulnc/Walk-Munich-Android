package com.github.eylulnc.walkmunich.feature.route.data

import android.content.Context
import com.github.eylulnc.walkmunich.core.data.model.RouteSummary
import com.github.eylulnc.walkmunich.core.data.model.RoutesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class RoutesServiceImpl(
    private val context: Context,
    private val json: Json
) : RoutesService {

    override suspend fun fetchRoutes(cityId: Long): List<RouteSummary> = withContext(Dispatchers.IO) {
        delay(300)
        val path = "api/routes_$cityId.json"
        val text = context.assets.open(path).bufferedReader().use { it.readText() }
        json.decodeFromString(RoutesResponse.serializer(), text).routes
    }
}


