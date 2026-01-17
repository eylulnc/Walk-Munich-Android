package com.github.eylulnc.walkmunich.feature.place.viewmodel

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

data class PlaceUiState(
    val isLoading: Boolean = true,
    val place: Place? = null,
    val subTitle: String? = null,
    val error: String? = null,
    val isFavorite: Boolean = false
)

class PlaceViewModel(
    private val repository: PlacesRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val placeId: Long,
    private val subTitle: String? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaceUiState())
    val uiState: StateFlow<PlaceUiState> = _uiState

    init {
        if (subTitle != null) _uiState.update { it.copy(subTitle = subTitle) }
        loadPlace()
        observeFavorite()
    }

    private fun loadPlace() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            runCatching {
                repository.getPlace(placeId)
            }.onSuccess { place ->
                _uiState.update { it.copy(isLoading = false, place = place) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun observeFavorite() {
        viewModelScope.launch {
            userPreferencesRepository.favoritePlaceIds.collectLatest { favoriteIds ->
                _uiState.update { it.copy(isFavorite = favoriteIds.contains(placeId.toString())) }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            userPreferencesRepository.toggleFavorite(placeId.toString())
        }
    }

    fun retry() {
        loadPlace()
    }
}
