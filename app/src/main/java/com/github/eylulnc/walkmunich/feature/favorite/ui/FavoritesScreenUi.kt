package com.github.eylulnc.walkmunich.feature.favorite.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.core.ui.composable.LoadingState
import com.github.eylulnc.walkmunich.core.ui.composable.PlaceCardSmall
import com.github.eylulnc.walkmunich.core.ui.composable.WMTopAppBarScreen
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes
import com.github.eylulnc.walkmunich.feature.favorite.viewmodel.FavoritesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoritesScreenUi(
    viewModel: FavoritesViewModel = koinViewModel(),
    onPlaceClick: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    WMTopAppBarScreen(
        title = "Favorites",
        onBack = null
    ) { modifier ->
        when {
            state.isLoading && state.favoritePlaces.isEmpty() -> LoadingState()
            state.favoritePlaces.isEmpty() -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No favorites yet",
                        fontSize = TypographySizes.medium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            else -> {
                LazyVerticalGrid(
                    modifier = modifier.fillMaxSize(),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(Spacing.Medium),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
                ) {
                    items(state.favoritePlaces) { place ->
                        PlaceCardSmall(
                            place = place,
                            onPlaceClick = { onPlaceClick(place.id) },
                            isFavorite = true,
                            onFavoriteClick = { viewModel.onToggleFavorite(place.id) }
                        )
                    }
                }
            }
        }
    }
}
