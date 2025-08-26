package com.github.eylulnc.walkmunich.feature.route.data

import com.github.eylulnc.walkmunich.core.data.model.RouteSummary

interface RoutesService {
    suspend fun fetchRoutes(cityId: Long): List<RouteSummary>
}


