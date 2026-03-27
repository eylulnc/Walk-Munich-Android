package com.github.eylulnc.walkmunich.feature.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.data.repository.PlacesRepository
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val Munich = LatLng(48.1351, 11.5820)

data class MapUiState(
    val places: List<Place> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedPlace: Place? = null,
    val savedCameraPosition: CameraPosition = CameraPosition.fromLatLngZoom(Munich, 12f),
    val hasCenteredCamera: Boolean = false
)

class MapViewModel(
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        loadPlaces()
    }

    fun selectPlace(place: Place) {
        _uiState.update { it.copy(selectedPlace = place) }
    }

    fun clearSelectedPlace() {
        _uiState.update { it.copy(selectedPlace = null) }
    }

    fun saveCameraPosition(position: CameraPosition) {
        _uiState.update { it.copy(savedCameraPosition = position) }
    }

    fun markCameraCentered() {
        _uiState.update { it.copy(hasCenteredCamera = true) }
    }

    private fun loadPlaces() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching {
                placesRepository.getPlaces()
            }.onSuccess { places ->
                _uiState.update { it.copy(places = places, isLoading = false) }
            }.onFailure { error ->
                _uiState.update { it.copy(error = error.message, isLoading = false) }
            }
        }
    }
}
