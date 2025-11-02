package com.github.eylulnc.walkmunich.feature.home.ui.category.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.core.ui.composable.CategoryChips
import com.github.eylulnc.walkmunich.core.ui.composable.ErrorState
import com.github.eylulnc.walkmunich.core.ui.composable.LoadingState
import com.github.eylulnc.walkmunich.core.ui.composable.PlaceCardSmall
import com.github.eylulnc.walkmunich.core.ui.composable.WMSearchTopAppBarScreen
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.feature.home.ui.SearchResultsSection
import com.github.eylulnc.walkmunich.feature.home.viewModel.HomeScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlacesOverviewScreenUi(
    viewModel: HomeScreenViewModel = koinViewModel(),
    onPlaceClick: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    WMSearchTopAppBarScreen(
        title = "All Places",
        searchQuery = state.searchQuery,
        onSearchQueryChange = viewModel::onQueryChange,
        onClearSearch = viewModel::onClearQuery,
        onBack = onBackClick
    ) { modifier ->
        when {
            state.isLoading -> LoadingState()
            state.error != null -> ErrorState(errorMessage = state.error)

            else -> {
                if (state.searchQuery.isNotBlank()) {
                    Column(modifier = modifier) {
                        SearchResultsSection(
                            searchResults = state.searchResults,
                            isSearching = state.isSearching,
                            onPlaceClick = { place -> onPlaceClick(place.id) }
                        )
                    }
                } else {
                    val placesToShow = when {
                        state.selectedCategory != null -> state.filteredPlaces
                        else -> state.allPlaces
                    }

                    Column(modifier = modifier) {
                        CategoryChips(
                            selectedCategory = state.selectedCategory,
                            onCategorySelected = viewModel::onCategorySelected
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(Spacing.Medium),
                            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
                            verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
                        ) {
                            items(placesToShow) { place ->
                                PlaceCardSmall(
                                    place = place,
                                    onPlaceClick = { onPlaceClick(place.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
