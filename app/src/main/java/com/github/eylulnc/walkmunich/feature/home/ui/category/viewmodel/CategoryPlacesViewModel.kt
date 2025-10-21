package com.github.eylulnc.walkmunich.feature.home.ui.category.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.eylulnc.walkmunich.core.data.model.Category
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.data.repository.PlacesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CategoryPlacesUiState(
    val isLoading: Boolean = true,
    val places: List<Place> = emptyList(),
    val error: String? = null,
    val category: Category? = null
)

class CategoryPlacesViewModel(
    private val repository: PlacesRepository,
    private val category: Category
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryPlacesUiState(category = category))
    val uiState: StateFlow<CategoryPlacesUiState> = _uiState

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            runCatching {
                repository.getPlaces(category)
            }.onSuccess { places ->
                _uiState.update { it.copy(isLoading = false, places = places) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}
