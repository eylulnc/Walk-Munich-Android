package com.github.eylulnc.walkmunich.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
    onPlaceClick: (Place) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (isSearching || searchResults.isEmpty()) Arrangement.Center else Arrangement.Top
    ) {
        when {
            isSearching -> {
                Text(
                    text = stringResource(R.string.searching),
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = TypographySizes.medium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = Spacing.Large)
                )
            }

            searchResults.isNotEmpty() -> {
                // 🟢 Grid layout with 2 columns
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        horizontal = Spacing.Medium,
                        vertical = Spacing.Medium
                    ),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
                ) {
                    items(searchResults) { searchResult ->
                        PlaceCardSmall(
                            place = searchResult.place,
                            onPlaceClick = { onPlaceClick(searchResult.place) }
                        )
                    }
                }
            }

            else -> {
                Text(
                    text = stringResource(R.string.no_search_results),
                    style = MaterialTheme.typography.headlineSmall,
                    fontSize = TypographySizes.medium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = Spacing.Large)
                )
            }
        }
    }
}
