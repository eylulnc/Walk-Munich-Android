package com.github.eylulnc.walkmunich.feature.route.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.eylulnc.walkmunich.core.data.model.RouteDetail
import com.github.eylulnc.walkmunich.feature.route.data.RoutesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RouteDetailUiState(
    val isLoading: Boolean = true,
    val routeDetail: RouteDetail? = null,
    val error: String? = null
)

class RouteDetailViewModel(
    private val repository: RoutesRepository,
    private val routeId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(RouteDetailUiState())
    val uiState: StateFlow<RouteDetailUiState> = _uiState

    init {
        loadRouteDetail()
    }

    private fun loadRouteDetail() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            runCatching {
                repository.getRouteDetail(routeId)
            }.onSuccess { routeDetail ->
                _uiState.update { it.copy(isLoading = false, routeDetail = routeDetail) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun retry() {
        loadRouteDetail()
    }
}
