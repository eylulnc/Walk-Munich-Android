package com.github.eylulnc.walkmunich.feature.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.data.repository.PlacesRepository
import com.github.eylulnc.walkmunich.core.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FavoritesScreenUiState(
    val isLoading: Boolean = false,
    val favoritePlaces: List<Place> = emptyList(),
    val favoritePlaceIds: Set<String> = emptySet(),
    val error: String? = null
)

class FavoritesViewModel(
    private val placesRepository: PlacesRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesScreenUiState())
    val uiState: StateFlow<FavoritesScreenUiState> = _uiState

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            userPreferencesRepository.favoritePlaceIds.collectLatest { favoriteIds ->
                _uiState.update { it.copy(favoritePlaceIds = favoriteIds, isLoading = true) }
                loadFavoritePlaces(favoriteIds)
            }
        }
    }

    private suspend fun loadFavoritePlaces(favoriteIds: Set<String>) {
        runCatching {
            // Instead of individual requests, fetch all and filter for efficiency
            val allPlaces = placesRepository.getPlaces()
            allPlaces.filter { favoriteIds.contains(it.id.toString()) }
        }.onSuccess { places ->
            _uiState.update { it.copy(favoritePlaces = places, isLoading = false) }
        }.onFailure { e ->
            _uiState.update { it.copy(error = e.message, isLoading = false) }
        }
    }

    fun onToggleFavorite(placeId: Long) {
        viewModelScope.launch {
            userPreferencesRepository.toggleFavorite(placeId.toString())
        }
    }
}
