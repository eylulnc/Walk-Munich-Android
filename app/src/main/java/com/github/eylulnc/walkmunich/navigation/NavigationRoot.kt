package com.github.eylulnc.walkmunich.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.github.eylulnc.walkmunich.feature.main.ui.MainScreenUi
import kotlinx.serialization.Serializable

@Serializable
data object MainScreen: NavKey

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(MainScreen)
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        entryProvider = { key ->
            when(key) {
                is MainScreen -> {
                    NavEntry(
                        key = key
                    ) {
                        MainScreenUi(
                            onCategoryClick = {},
                            onFavoritesItemClick = {}
                        )
                    }
                }
                else -> throw RuntimeException("Invalid NavKey.")
            }
        },
    )
}