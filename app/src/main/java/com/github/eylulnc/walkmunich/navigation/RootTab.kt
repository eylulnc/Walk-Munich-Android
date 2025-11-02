package com.github.eylulnc.walkmunich.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.ui.graphics.vector.ImageVector

sealed class RootTab(val label: String, val icon: ImageVector) {
    data object Explore : RootTab("Explore", Icons.Filled.Explore)
    data object Tours : RootTab("Tours", Icons.Outlined.ConfirmationNumber)
    data object Favorites : RootTab("Favorites", Icons.Filled.Favorite)
    data object Profile : RootTab("Profile", Icons.Filled.Person)
}
