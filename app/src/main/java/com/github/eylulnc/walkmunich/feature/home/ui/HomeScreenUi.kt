package com.github.eylulnc.walkmunich.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.ui.composable.ErrorState
import com.github.eylulnc.walkmunich.core.ui.composable.LoadingState
import com.github.eylulnc.walkmunich.core.ui.composable.PlaceCard
import com.github.eylulnc.walkmunich.core.ui.composable.WMSearchTopAppBarScreen
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes
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
                            onPlaceClick = { place -> onPlaceItemClick(place.id) }
                        )
                    }
                } else {
                    Column(
                        modifier = modifier.verticalScroll(rememberScrollState())
                    ) {
                        PlacesSection(
                            places = state.allPlaces,
                            onPlaceClick = onPlaceItemClick,
                            onSeeAllClick = onSeeAllPlacesClick
                        )

                        Spacer(modifier = Modifier.height(Spacing.Large))

                        state.highlightedPlace?.let {
                            HighlightSection(place = it)
                        }

                        Spacer(modifier = Modifier.height(Spacing.Large))

                        FavoritesSection(
                            onSeeAllClick = onSeeAllFavoritesClick,
                            favorites = state.filteredPlaces,
                            onPlaceClick = onPlaceItemClick
                        )

                        Spacer(modifier = Modifier.height(Spacing.Large))
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
        SectionHeader(title = "Places", onSeeAllClick = onSeeAllClick)

        LazyRow(
            contentPadding = PaddingValues(horizontal = Spacing.Medium),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
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
        SectionHeader(title = "Your Favorites", onSeeAllClick = onSeeAllClick)

        LazyRow(
            contentPadding = PaddingValues(horizontal = Spacing.Medium),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            items(favorites.take(5)) { place ->
                PlaceCard(
                    place = place,
                    onPlaceClick = { onPlaceClick(place.id) },
                    isFavorite = true
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.Medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = TypographySizes.subtitle,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        TextButton(onClick = onSeeAllClick) {
            Text(
                text = "See All",
                color = MaterialTheme.colorScheme.primary,
                fontSize = TypographySizes.body
            )
        }
    }
}

@Composable
fun HighlightSection(place: Place) {
    Column(modifier = Modifier.padding(horizontal = Spacing.Medium)) {
        Text(
            text = "Highlight of the Day",
            fontSize = TypographySizes.subtitle,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(Spacing.Small))
        HighlightCard(place = place)
    }
}

@Composable
fun HighlightCard(place: Place) {
    val imageResId = ImageResolver.resolveDrawable(place.imageUrl)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Spacing.CornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.Small)
    ) {
        Box(modifier = Modifier.height(Spacing.HeroHeight / 1.5f)) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Highlight Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )

            Box(
                modifier = Modifier
                    .padding(Spacing.Small)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .padding(Spacing.ExtraSmall)
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(Spacing.Medium)
            ) {
                Text(
                    text = place.name,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = TypographySizes.subtitle
                )
            }
        }
    }
}
