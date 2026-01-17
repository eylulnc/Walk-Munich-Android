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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.ui.composable.ErrorState
import com.github.eylulnc.walkmunich.core.ui.composable.FavoriteButton
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
                            favoriteIds = state.favoritePlaceIds,
                            onPlaceClick = { place -> onPlaceItemClick(place.id) },
                            onFavoriteClick = viewModel::onToggleFavorite
                        )
                    }
                } else {
                    Column(
                        modifier = modifier.verticalScroll(rememberScrollState())
                    ) {
                        PlacesSection(
                            places = state.allPlaces,
                            favoriteIds = state.favoritePlaceIds,
                            onPlaceClick = onPlaceItemClick,
                            onSeeAllClick = onSeeAllPlacesClick,
                            onFavoriteClick = viewModel::onToggleFavorite
                        )

                        Spacer(modifier = Modifier.height(Spacing.Large))

                        state.highlightedPlace?.let {
                            HighlightSection(
                                place = it,
                                isFavorite = state.favoritePlaceIds.contains(it.id.toString()),
                                onFavoriteClick = { viewModel.onToggleFavorite(it.id) }
                            )
                        }

                        Spacer(modifier = Modifier.height(Spacing.Large))

                        FavoritesSection(
                            onSeeAllClick = onSeeAllFavoritesClick,
                            allPlaces = state.allPlaces,
                            favoriteIds = state.favoritePlaceIds,
                            onPlaceClick = onPlaceItemClick,
                            onFavoriteClick = viewModel::onToggleFavorite
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
    favoriteIds: Set<String>,
    onPlaceClick: (Long) -> Unit,
    onSeeAllClick: () -> Unit,
    onFavoriteClick: (Long) -> Unit
) {
    Column {
        SectionHeader(title = "Places", onSeeAllClick = onSeeAllClick)

        LazyRow(
            contentPadding = PaddingValues(horizontal = Spacing.Medium),
            horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
        ) {
            items(places.take(5)) { place ->
                PlaceCard(
                    place = place,
                    onPlaceClick = { onPlaceClick(place.id) },
                    isFavorite = favoriteIds.contains(place.id.toString()),
                    onFavoriteClick = { onFavoriteClick(place.id) }
                )
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
                        onFavoriteClick = { onFavoriteClick(place.id) }
                    )
                }
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
fun HighlightSection(
    place: Place,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = Spacing.Medium)) {
        Text(
            text = "Highlight of the Day",
            fontSize = TypographySizes.subtitle,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(Spacing.Small))
        HighlightCard(
            place = place,
            isFavorite = isFavorite,
            onFavoriteClick = onFavoriteClick
        )
    }
}

@Composable
fun HighlightCard(
    place: Place,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
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

            FavoriteButton(isFavorite, onFavoriteClick)

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
