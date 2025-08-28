package com.github.eylulnc.walkmunich.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.data.model.SearchResult
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes
import com.github.eylulnc.walkmunich.core.ui.composable.PlaceOverviewCard

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
            .fillMaxHeight()
    ) {
        if (isSearching) {
            // Show searching indicator
            Text(
                text = stringResource(R.string.searching),
                style = MaterialTheme.typography.headlineSmall,
                fontSize = TypographySizes.large,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = Spacing.Large, vertical = Spacing.Medium)
            )
        } else if (searchResults.isNotEmpty()) {
            Text(
                text = stringResource(R.string.search_results, searchResults.size),
                style = MaterialTheme.typography.headlineSmall,
                fontSize = TypographySizes.large,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = Spacing.Large, vertical = Spacing.Medium)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = Spacing.Large, vertical = Spacing.Medium),
                verticalArrangement = Arrangement.spacedBy(Spacing.ItemGap)
            ) {
                items(searchResults) { searchResult ->
                    PlaceOverviewCard(
                        place = searchResult.place,
                        onClick = { onPlaceClick(searchResult.place) }
                    )
                }
            }
        } else {
            // Only show "No results" when search is complete and no results found
            Text(
                text = stringResource(R.string.no_search_results),
                style = MaterialTheme.typography.headlineSmall,
                fontSize = TypographySizes.large,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = Spacing.Large, vertical = Spacing.Medium)
            )
        }
    }
}

