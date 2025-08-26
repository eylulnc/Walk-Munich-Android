package com.github.eylulnc.walkmunich.feature.route.data

import com.github.eylulnc.walkmunich.core.data.model.RouteSummary

class RoutesRepository(
    private val service: RoutesService
) {
    suspend fun getRoutes(cityId: Long): List<RouteSummary> = service.fetchRoutes(cityId)
}


