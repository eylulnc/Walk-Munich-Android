package com.github.eylulnc.walkmunich.navigation

import androidx.navigation3.runtime.NavKey
import com.github.eylulnc.walkmunich.core.data.model.Category
import kotlinx.serialization.Serializable

@Serializable data object HomeScreen : NavKey
@Serializable data object RouteListScreen : NavKey
@Serializable data object FavoritesScreen : NavKey
@Serializable data object SettingsScreen : NavKey
@Serializable data class RouteDetailScreen(val routeId: Long) : NavKey
@Serializable data class PlaceDetailScreen(val placeId: Long, val subTitle: String? = null) : NavKey
@Serializable data class CategoryPlacesScreen(val category: Category) : NavKey
