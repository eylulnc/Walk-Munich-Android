package com.github.eylulnc.walkmunich.feature.place.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.data.repository.PlacesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlaceUiState(
    val isLoading: Boolean = true,
    val place: Place? = null,
    val subTitle: String? = null,
    val error: String? = null
)

class PlaceViewModel(
    private val repository: PlacesRepository,
    private val placeId: Long,
    private val subTitle: String? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaceUiState())
    val uiState: StateFlow<PlaceUiState> = _uiState

    init {
        if (subTitle != null) _uiState.update { it.copy(subTitle = subTitle) }
        loadPlace()
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

    fun retry() {
        loadPlace()
    }
}
