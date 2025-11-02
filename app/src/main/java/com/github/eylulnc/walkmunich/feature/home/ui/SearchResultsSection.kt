package com.github.eylulnc.walkmunich.feature.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
                    fontSize = TypographySizes.large,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = Spacing.Large)
                )
            }

            searchResults.isNotEmpty() -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = Spacing.Large, vertical = Spacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(Spacing.ItemGap)
                ) {
                    items(searchResults) { searchResult ->
                        PlaceCard(
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
                    fontSize = TypographySizes.large,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = Spacing.Large)
                )
            }
        }
    }
}
