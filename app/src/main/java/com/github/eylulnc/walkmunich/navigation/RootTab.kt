package com.github.eylulnc.walkmunich.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Route
import androidx.compose.ui.graphics.vector.ImageVector

sealed class RootTab(val label: String, val icon: ImageVector) {
    data object Home : RootTab("Home", Icons.Outlined.Home)
    data object Route : RootTab("Route", Icons.Outlined.Route)
    data object Favorites : RootTab("Favorites", Icons.Outlined.FavoriteBorder)
}

val allTabs = listOf(
    RootTab.Home, RootTab.Route, RootTab.Favorites
)
