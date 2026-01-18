package com.github.eylulnc.walkmunich.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object HomeScreen : NavKey
@Serializable data object ToursScreen : NavKey
@Serializable data object FavoritesScreen : NavKey
@Serializable data object SettingsScreen : NavKey
@Serializable data object AllPlacesScreen : NavKey
@Serializable object AppDescriptionScreen : NavKey
@Serializable object DisclaimerScreen : NavKey
@Serializable object AttributionScreen : NavKey
@Serializable object ImpressumScreen : NavKey

@Serializable data class RouteDetailScreen(val routeId: Long) : NavKey
@Serializable data class PlaceDetailScreen(val placeId: Long, val subTitle: String? = null) : NavKey