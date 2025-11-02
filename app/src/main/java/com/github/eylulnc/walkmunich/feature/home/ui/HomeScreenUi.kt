package com.github.eylulnc.walkmunich.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.ui.composable.ErrorState
import com.github.eylulnc.walkmunich.core.ui.composable.LoadingState
import com.github.eylulnc.walkmunich.core.ui.composable.PlaceCard
import com.github.eylulnc.walkmunich.core.ui.composable.WMSearchTopAppBarScreen
import com.github.eylulnc.walkmunich.core.ui.util.ImageResolver
import com.github.eylulnc.walkmunich.feature.home.viewModel.HomeScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenUi(
    viewModel: HomeScreenViewModel = koinViewModel(),
    onPlaceItemClick: (Long) -> Unit,
    onSeeAllFavoritesClick: () -> Unit,
    onSeeAllPlacesClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    WMSearchTopAppBarScreen(
        title = "Walk Munich",
        searchQuery = state.searchQuery,
        onSearchQueryChange = viewModel::onQueryChange,
        onClearSearch = viewModel::onClearQuery
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
                            onPlaceClick = { place ->
                                onPlaceItemClick(place.id)
                            }
                        )
                    }
                } else {
                    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
                        PlacesSection(
                            places = state.allPlaces,
                            onPlaceClick = onPlaceItemClick,
                            onSeeAllClick = onSeeAllPlacesClick
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        state.highlightedPlace?.let { HighlightSection(it) }
                        Spacer(modifier = Modifier.height(24.dp))
                        FavoritesSection(
                            onSeeAllClick = onSeeAllFavoritesClick,
                            favorites = state.filteredPlaces,
                            onPlaceClick = onPlaceItemClick
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun PlacesSection(
    places: List<Place>,
    onPlaceClick: (Long) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Places", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = { onSeeAllClick() }) {
                Text("See All", color = MaterialTheme.colorScheme.primary)
            }
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(places.take(5)) { place ->
                PlaceCard(place = place, onPlaceClick = { onPlaceClick(place.id) })
            }
        }
    }
}

@Composable
fun FavoritesSection(
    onSeeAllClick: () -> Unit,
    favorites: List<Place>,
    onPlaceClick: (Long) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Your Favorites", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = onSeeAllClick) {
                Text("See All", color = MaterialTheme.colorScheme.primary)
            }
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(favorites.take(5)) { place ->
                PlaceCard(place = place, onPlaceClick = { onPlaceClick(place.id) }, isFavorite = true)
            }
        }
    }
}

@Composable
fun HighlightSection(place: Place) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(text = "Highlight of the Day", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        HighlightCard(place = place)
    }
}

@Composable
fun HighlightCard(place: Place) {
    val imageResId = ImageResolver.resolveDrawable(place.imageUrl)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Highlight Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.Black.copy(alpha = 0.4f)
                    )
            )
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .padding(4.dp)
            ) {
                Icon(
                    Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color.White
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    place.name,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HomeScreenUi(
        onPlaceItemClick = {},
        onSeeAllFavoritesClick = {},
        onSeeAllPlacesClick = {}
    )
}
