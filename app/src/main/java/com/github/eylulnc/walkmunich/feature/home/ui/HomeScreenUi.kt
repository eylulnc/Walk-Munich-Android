package com.github.eylulnc.walkmunich.feature.home.ui

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
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.ui.composable.ErrorState
import com.github.eylulnc.walkmunich.core.ui.composable.FavoriteButton
import com.github.eylulnc.walkmunich.core.ui.composable.LoadingState
import com.github.eylulnc.walkmunich.core.ui.composable.PlaceCard
import com.github.eylulnc.walkmunich.core.ui.composable.PlaceImage
import com.github.eylulnc.walkmunich.core.ui.composable.WMSearchTopAppBarScreen
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes
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
        title = "Walk Munich"
    ) { modifier ->
        when {
            state.isLoading -> LoadingState()
            state.error != null -> ErrorState(errorMessage = state.error)
            else -> {
                if (state.searchQuery.isNotBlank()) {
                    // Search field lives inside the grid as a full-span header
                    // so it scrolls away with the results
                    SearchResultsSection(
                        modifier = modifier,
                        searchResults = state.searchResults,
                        isSearching = state.isSearching,
                        favoriteIds = state.favoritePlaceIds,
                        onPlaceClick = { place -> onPlaceItemClick(place.id) },
                        onFavoriteClick = viewModel::onToggleFavorite,
                        header = {
                            HomeSearchField(
                                query = state.searchQuery,
                                onQueryChange = viewModel::onQueryChange,
                                onClear = viewModel::onClearQuery
                            )
                        })
                } else {
                    // Normal mode: search field is the first item in the scroll —
                    // it disappears naturally when the user scrolls down
                    Column(
                        modifier = modifier.verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(Spacing.Small)
                        ) {
                            HomeSearchField(
                                query = state.searchQuery,
                                onQueryChange = viewModel::onQueryChange,
                                onClear = viewModel::onClearQuery
                            )
                        }


                        Spacer(modifier = Modifier.height(Spacing.Small))

                        PlacesSection(
                            places = state.allPlaces,
                            favoriteIds = state.favoritePlaceIds,
                            onPlaceClick = onPlaceItemClick,
                            onSeeAllClick = onSeeAllPlacesClick,
                            onFavoriteClick = viewModel::onToggleFavorite
                        )

                        Spacer(modifier = Modifier.height(Spacing.Medium))

                        state.highlightedPlace?.let {
                            HighlightSection(
                                place = it,
                                isFavorite = state.favoritePlaceIds.contains(it.id.toString()),
                                onFavoriteClick = { viewModel.onToggleFavorite(it.id) })
                        }

                        Spacer(modifier = Modifier.height(Spacing.Medium))

                        FavoritesSection(
                            onSeeAllClick = onSeeAllFavoritesClick,
                            allPlaces = state.allPlaces,
                            favoriteIds = state.favoritePlaceIds,
                            onPlaceClick = onPlaceItemClick,
                            onFavoriteClick = viewModel::onToggleFavorite
                        )

                        if (state.recentlyViewedPlaceIds.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(Spacing.Medium))

                            RecentlyViewedSection(
                                allPlaces = state.allPlaces,
                                recentlyViewedIds = state.recentlyViewedPlaceIds,
                                favoriteIds = state.favoritePlaceIds,
                                onPlaceClick = onPlaceItemClick,
                                onFavoriteClick = viewModel::onToggleFavorite
                            )
                        }

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
    favoriteIds: Set<String>,
    onPlaceClick: (Long) -> Unit,
    onSeeAllClick: () -> Unit,
    onFavoriteClick: (Long) -> Unit
) {
    Column {
        SectionHeader(title = "Explore Munich", onSeeAllClick = onSeeAllClick)

        LazyRow(
            contentPadding = PaddingValues(horizontal = Spacing.Medium),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            items(places.take(5)) { place ->
                PlaceCard(
                    place = place,
                    onPlaceClick = { onPlaceClick(place.id) },
                    isFavorite = favoriteIds.contains(place.id.toString()),
                    onFavoriteClick = { onFavoriteClick(place.id) })
            }
        }
    }
}

@Composable
fun FavoritesSection(
    onSeeAllClick: () -> Unit,
    allPlaces: List<Place>,
    favoriteIds: Set<String>,
    onPlaceClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit
) {
    val favoritePlaces = allPlaces.filter { favoriteIds.contains(it.id.toString()) }

    if (favoritePlaces.isNotEmpty()) {
        Column {
            SectionHeader(title = "Your Favorites", onSeeAllClick = onSeeAllClick)

            LazyRow(
                contentPadding = PaddingValues(horizontal = Spacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
            ) {
                items(favoritePlaces.take(5)) { place ->
                    PlaceCard(
                        place = place,
                        onPlaceClick = { onPlaceClick(place.id) },
                        isFavorite = true,
                        onFavoriteClick = { onFavoriteClick(place.id) })
                }
            }
        }
    }
}

@Composable
fun RecentlyViewedSection(
    allPlaces: List<Place>,
    recentlyViewedIds: List<String>,
    favoriteIds: Set<String>,
    onPlaceClick: (Long) -> Unit,
    onFavoriteClick: (Long) -> Unit
) {
    // Map IDs to Place objects, maintaining the order from recentlyViewedIds
    val recentlyViewedPlaces = recentlyViewedIds.mapNotNull { id ->
        allPlaces.find { it.id.toString() == id }
    }

    if (recentlyViewedPlaces.isNotEmpty()) {
        Column {
            Text(
                text = "Recently Viewed",
                fontSize = TypographySizes.subtitle,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = Spacing.Medium, vertical = Spacing.Small)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = Spacing.Medium),
                horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
            ) {
                items(recentlyViewedPlaces) { place ->
                    PlaceCard(
                        place = place,
                        onPlaceClick = { onPlaceClick(place.id) },
                        isFavorite = favoriteIds.contains(place.id.toString()),
                        onFavoriteClick = { onFavoriteClick(place.id) })
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String, onSeeAllClick: () -> Unit
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
fun HighlightSection(
    place: Place, isFavorite: Boolean, onFavoriteClick: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = Spacing.Medium)) {
        Text(
            text = "Highlight of the Day",
            fontSize = TypographySizes.subtitle,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = Spacing.Small)
        )
        Spacer(modifier = Modifier.height(Spacing.Small))
        HighlightCard(
            place = place, isFavorite = isFavorite, onFavoriteClick = onFavoriteClick
        )
    }
}

@Composable
fun HighlightCard(
    place: Place, isFavorite: Boolean, onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Spacing.CornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = Spacing.Small)
    ) {
        Box(modifier = Modifier.height(Spacing.HeroHeight / 1.5f)) {
            PlaceImage(
                imageUrl = place.imageUrl,
                contentDescription = place.name,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.45f to Color.Transparent,
                                1.0f to Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            FavoriteButton(isFavorite, onFavoriteClick)

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(Spacing.Medium)
            ) {
                place.story?.mainTitle?.let { mainTitle ->
                    Text(
                        text = mainTitle,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = TypographySizes.small
                    )
                }
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

@Composable
private fun HomeSearchField(
    query: String, onQueryChange: (String) -> Unit, onClear: () -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search places...",
                fontSize = TypographySizes.body,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface, fontSize = TypographySizes.body
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        },
        singleLine = true,
        shape = CircleShape,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(Spacing.SearchBarHeight)
            .padding(horizontal = Spacing.Medium)
    )
}
