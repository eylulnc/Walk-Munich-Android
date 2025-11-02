package com.github.eylulnc.walkmunich.feature.home.ui.category.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.core.ui.composable.ErrorState
import com.github.eylulnc.walkmunich.core.ui.composable.LoadingState
import com.github.eylulnc.walkmunich.core.ui.composable.PlaceCard
import com.github.eylulnc.walkmunich.core.ui.composable.WMTopAppBarScreen
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.feature.home.ui.category.viewmodel.CategoryPlacesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CategoryPlacesScreenUi(
    viewModel: CategoryPlacesViewModel = koinViewModel(),
    onPlaceClick: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    WMTopAppBarScreen(
        title = state.category?.name ?: "Category",
        onBack = onBackClick
    ) { modifier ->
        when {
            state.isLoading -> LoadingState()
            state.error != null -> ErrorState(errorMessage = state.error)
            else -> {
                LazyColumn(
                    modifier = modifier,
                    contentPadding = PaddingValues(Spacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
                ) {
                    items(state.places) { place ->
                        PlaceCard(
                            place = place,
                            onPlaceClick = { onPlaceClick(place.id) }
                        )
                    }
                }
            }
        }
    }
}
