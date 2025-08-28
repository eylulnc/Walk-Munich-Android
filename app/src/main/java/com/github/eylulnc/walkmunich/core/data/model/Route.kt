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

@Serializable
data class RouteDetail(
    val id: Long,
    val cityId: Long,
    val title: String,
    val segments: List<RouteSegment>,
    val updatedAt: String
)

@Serializable
data class RouteSegment(
    val id: Long,
    val title: String,
    val dayIndex: Int,
    val stops: List<RouteStop>
)

@Serializable
data class RouteStop(
    val ord: Int,
    val placeId: Long,
    val name: String,
    val category: Category
)

