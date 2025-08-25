package com.github.eylulnc.walkmunich.feature.main.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.data.model.SearchResult
import com.github.eylulnc.walkmunich.core.data.model.toUi
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes
import com.github.eylulnc.walkmunich.core.ui.util.ImageResolver

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
                    SearchResultCard(
                        searchResult = searchResult,
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

@Composable
private fun SearchResultCard(
    searchResult: SearchResult,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Spacing.Small
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //  Place image - use actual place image if available, fallback to hero image
            // TODO add related images
            val imageResId = try {
                ImageResolver.resolveDrawable(searchResult.place.imageUrl)
            } catch (e: Exception) {
                R.drawable.hero_munich // fallback
            }
            
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = searchResult.place.name,
                modifier = Modifier
                    .size(Spacing.SearchBarImageSize)
                    .clip(RoundedCornerShape(Spacing.Small)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(Spacing.Medium))
            
            // Place details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = searchResult.place.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(Spacing.ExtraSmall))

                val category = searchResult.category.toUi()
                Text(
                    text = stringResource(category.titleResource),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}
