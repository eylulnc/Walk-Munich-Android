package com.github.eylulnc.walkmunich.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.github.eylulnc.walkmunich.feature.home.ui.MainScreenUi
import com.github.eylulnc.walkmunich.feature.route.ui.RouteDetailScreenUi
import com.github.eylulnc.walkmunich.feature.route.ui.RouteListScreenUi
import com.github.eylulnc.walkmunich.core.ui.theme.OrangeMain
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val homeBackStack = rememberNavBackStack(MainScreen)
    val routeBackStack = rememberNavBackStack(RouteListScreen)
    val favBackStack = rememberNavBackStack(FavoritesScreen)

    // Order of tabs shown in the bar
    val tabs = remember { listOf(RootTab.Home, RootTab.Route, RootTab.Favorites) }

    // Save an Int instead of the sealed object
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val currentTab = tabs[selectedTabIndex]

    val currentBackStack = when (currentTab) {
        RootTab.Home -> homeBackStack
        RootTab.Route -> routeBackStack
        RootTab.Favorites -> favBackStack
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            Column {
                HorizontalDivider(thickness = Spacing.BorderStroke, color = Color.LightGray)
                NavigationBar(containerColor = Color.White) {
                    tabs.forEachIndexed { index, tab ->
                        NavigationBarItem(
                            selected = index == selectedTabIndex,
                            onClick = {
                                selectedTabIndex = index
                            },
                            icon = { Icon(tab.icon, contentDescription = null) },
                            label = { Text(tab.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = OrangeMain,
                                selectedTextColor = OrangeMain,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = currentBackStack,
            entryDecorators = listOf(
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
                rememberSceneSetupNavEntryDecorator()
            ),
            entryProvider = { key ->
                when (key) {
                    is MainScreen -> {
                        NavEntry(key = key) {
                            MainScreenUi(
                                onCategoryClick = { /* TODO */ },
                                onPlaceItemClick = { /* TODO */ }
                            )
                        }
                    }

                    is RouteListScreen -> {
                        NavEntry(key = key) {
                            RouteListScreenUi(
                                onRouteClick = { routeId ->
                                    routeBackStack.add(RouteDetailScreen(routeId))
                                }
                            )
                        }
                    }

                    is RouteDetailScreen -> {
                        NavEntry(key = key) {
                            RouteDetailScreenUi(
                                viewModel = koinViewModel {
                                    parametersOf(key.routeId)
                                },
                                onBackClick = { routeBackStack.remove(key) }
                            )
                        }
                    }

                    is FavoritesScreen -> {
                        NavEntry(key = key) {
                            Text(
                                "Favorites",
                                modifier = Modifier.safeContentPadding()
                            )
                        }
                    }

                    else -> error("Invalid NavKey.")
                }
            }
        )
    }
}
