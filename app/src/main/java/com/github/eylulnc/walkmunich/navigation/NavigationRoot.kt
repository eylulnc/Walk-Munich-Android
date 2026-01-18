package com.github.eylulnc.walkmunich.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.ui.composable.SettingsTextDetailScreen
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.feature.favorite.ui.FavoritesScreenUi
import com.github.eylulnc.walkmunich.feature.home.ui.HomeScreenUi
import com.github.eylulnc.walkmunich.feature.home.ui.PlacesOverviewScreenUi
import com.github.eylulnc.walkmunich.feature.settings.SettingsScreenUi
import com.github.eylulnc.walkmunich.feature.place.ui.PlaceDetailScreenUi
import com.github.eylulnc.walkmunich.feature.route.ui.RouteDetailScreenUi
import com.github.eylulnc.walkmunich.feature.route.ui.RouteListScreenUi
import com.github.eylulnc.walkmunich.feature.settings.AboutScreenUi
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val exploreBackStack = rememberNavBackStack(HomeScreen)
    val toursBackStack = rememberNavBackStack(ToursScreen)
    val favoritesBackStack = rememberNavBackStack(FavoritesScreen)
    val settingsBackStack = rememberNavBackStack(SettingsScreen)

    // Order of tabs shown in the bar
    val tabs =
        remember { listOf(RootTab.Explore, RootTab.Tours, RootTab.Favorites, RootTab.Settings) }

    // Save an Int instead of the sealed object
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val currentTab = tabs[selectedTabIndex]

    val currentBackStack = when (currentTab) {
        RootTab.Explore -> exploreBackStack
        RootTab.Tours -> toursBackStack
        RootTab.Favorites -> favoritesBackStack
        RootTab.Settings -> settingsBackStack
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            Column {
                HorizontalDivider(
                    thickness = Spacing.BorderStroke,
                    color = MaterialTheme.colorScheme.outline
                )
                NavigationBar(containerColor = MaterialTheme.colorScheme.background) {
                    tabs.forEachIndexed { index, tab ->
                        NavigationBarItem(
                            selected = index == selectedTabIndex,
                            onClick = {
                                selectedTabIndex = index
                            },
                            icon = { Icon(tab.icon, contentDescription = null) },
                            label = { Text(tab.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    is HomeScreen -> {
                        NavEntry(key = key) {
                            HomeScreenUi(
                                onPlaceItemClick = { placeId ->
                                    exploreBackStack.add(
                                        PlaceDetailScreen(
                                            placeId
                                        )
                                    )
                                },
                                onSeeAllFavoritesClick = {
                                    selectedTabIndex = tabs.indexOf(RootTab.Favorites)
                                },
                                onSeeAllPlacesClick = {
                                    exploreBackStack.add(AllPlacesScreen)
                                }
                            )
                        }
                    }

                    is ToursScreen -> {
                        NavEntry(key = key) {
                            RouteListScreenUi(
                                onRouteClick = { routeId ->
                                    toursBackStack.add(RouteDetailScreen(routeId))
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
                                onBackClick = { toursBackStack.remove(key) },
                                onPlaceItemClick = { placeId, subTitle ->
                                    toursBackStack.add(
                                        PlaceDetailScreen(
                                            placeId,
                                            subTitle
                                        )
                                    )
                                }
                            )
                        }
                    }

                    is FavoritesScreen -> {
                        NavEntry(key = key) {
                            FavoritesScreenUi(
                                onPlaceClick = { placeId ->
                                    favoritesBackStack.add(PlaceDetailScreen(placeId))
                                }
                            )
                        }
                    }

                    is PlaceDetailScreen -> {
                        NavEntry(key = key) {
                            PlaceDetailScreenUi(
                                viewModel = koinViewModel {
                                    parametersOf(key.placeId, key.subTitle)
                                },
                                onBackClick = {
                                    currentBackStack.remove(key)
                                }
                            )
                        }
                    }

                    is SettingsScreen -> {
                        NavEntry(key = key) {
                            SettingsScreenUi(
                                onAboutClick = {
                                    settingsBackStack.add(AboutScreen)
                                }
                            )
                        }
                    }

                    is AboutScreen -> {
                        NavEntry(key = key) {
                            AboutScreenUi(
                                onOpenAppDescription = { settingsBackStack.add(AppDescriptionScreen) },
                                onOpenDisclaimer = { settingsBackStack.add(DisclaimerScreen) },
                                onOpenAttribution = { settingsBackStack.add(AttributionScreen) },
                                onOpenImpressum = { settingsBackStack.add(ImpressumScreen) },
                                onBackClick = {
                                    currentBackStack.remove(key)
                                }
                            )
                        }
                    }

                    is AppDescriptionScreen -> {
                        NavEntry(key = key) {
                            SettingsTextDetailScreen(
                                title = stringResource(R.string.about_app_description_title),
                                content = stringResource(R.string.about_app_description),
                                onBackClick = { settingsBackStack.remove(key) }
                            )
                        }
                    }

                    is DisclaimerScreen -> {
                        NavEntry(key = key) {
                            SettingsTextDetailScreen(
                                title = stringResource(R.string.about_disclaimer_title),
                                content = stringResource(R.string.about_disclaimer),
                                onBackClick = { settingsBackStack.remove(key) }
                            )
                        }
                    }

                    is AttributionScreen -> {
                        NavEntry(key = key) {
                            SettingsTextDetailScreen(
                                title = stringResource(R.string.about_attribution_title),
                                content = stringResource(R.string.about_attribution),
                                onBackClick = { settingsBackStack.remove(key) }
                            )
                        }
                    }

                    is ImpressumScreen -> {
                        NavEntry(key = key) {
                            SettingsTextDetailScreen(
                                title = stringResource(R.string.about_impressum_title),
                                content = stringResource(R.string.about_impressum),
                                onBackClick = { settingsBackStack.remove(key) }
                            )
                        }
                    }


                    is AllPlacesScreen -> {
                        NavEntry(key = key) {
                            PlacesOverviewScreenUi(
                                onPlaceClick = { placeId ->
                                    currentBackStack.add(PlaceDetailScreen(placeId, null))
                                },
                                onBackClick = {
                                    currentBackStack.remove(key)
                                }
                            )
                        }
                    }

                    else -> error("Invalid NavKey. Got $key")
                }
            }
        )
    }
}
