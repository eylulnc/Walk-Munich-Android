package com.github.eylulnc.walkmunich.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object HomeScreen : NavKey
@Serializable data object RouteListScreen : NavKey
@Serializable data object FavoritesScreen : NavKey
@Serializable data class RouteDetailScreen(val routeId: Long) : NavKey
