package com.github.eylulnc.walkmunich.feature.route.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.eylulnc.walkmunich.core.data.model.RouteSummary
import com.github.eylulnc.walkmunich.feature.route.data.RoutesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RouteListUiState(
    val isLoading: Boolean = true,
    val routes: List<RouteSummary> = emptyList(),
    val error: String? = null
)

class RouteListViewModel(
    private val repository: RoutesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RouteListUiState())
    val uiState: StateFlow<RouteListUiState> = _uiState

    init {
        load()
    }

    fun load(cityId: Long = 1L) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            runCatching {
                repository.getRoutes(cityId)
            }.onSuccess { data ->
                _uiState.update { it.copy(isLoading = false, routes = data) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}


