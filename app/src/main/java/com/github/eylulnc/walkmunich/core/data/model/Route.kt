package com.github.eylulnc.walkmunich.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RouteSummary(
    val id: Long,
    val title: String,
    val summary: String? = null
)

@Serializable
data class RoutesResponse(
    val cityId: Long,
    val routes: List<RouteSummary>
)

data class RouteDetail(
    val id: Long,
    val cityId: Long,
    val title: String,
    val targetBreakEveryMin: Int?,
    val maxDetourMin: Int?,
    val paceMinPerKm: Int?,
    val segments: List<RouteSegment>,
    val updatedAt: String
)

data class RouteSegment(
    val id: Long,
    val title: String,
    val dayIndex: Int,
    val stops: List<RouteStop>
)

data class RouteStop(
    val ord: Int,
    val placeId: Long,
    val estDurationMin: Int?,
    val isBreak: Boolean,
    val detourMinutes: Int?
)

