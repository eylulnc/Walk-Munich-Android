package com.github.eylulnc.walkmunich.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.data.model.SearchResult
import com.github.eylulnc.walkmunich.core.ui.composable.PlaceCardSmall
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes

@Composable
fun SearchResultsSection(
    modifier: Modifier = Modifier,
    searchResults: List<SearchResult>,
    isSearching: Boolean = false,
    favoriteIds: Set<String> = emptySet(),
    onPlaceClick: (Place) -> Unit,
    onFavoriteClick: (Long) -> Unit = {},
    // Search bar passed as a header so it scrolls with the grid
    header: @Composable () -> Unit = {}
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            horizontal = Spacing.Medium,
            vertical = Spacing.Small
        ),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        // Search bar — full width, scrolls with the list
        item(span = { GridItemSpan(maxLineSpan) }) {
            Box(modifier = Modifier.padding(bottom = Spacing.Small)) {
                header()
            }
        }

        when {
            isSearching -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.searching),
                            style = MaterialTheme.typography.headlineSmall,
                            fontSize = TypographySizes.medium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            searchResults.isEmpty() -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_search_results),
                            style = MaterialTheme.typography.headlineSmall,
                            fontSize = TypographySizes.medium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            else -> {
                items(searchResults) { searchResult ->
                    PlaceCardSmall(
                        place = searchResult.place,
                        onPlaceClick = { onPlaceClick(searchResult.place) },
                        isFavorite = favoriteIds.contains(searchResult.place.id.toString()),
                        onFavoriteClick = { onFavoriteClick(searchResult.place.id) }
                    )
                }
            }
        }
    }
}
