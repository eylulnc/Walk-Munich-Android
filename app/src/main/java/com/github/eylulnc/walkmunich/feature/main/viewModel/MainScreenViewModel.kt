package com.github.eylulnc.walkmunich.feature.main.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.eylulnc.walkmunich.core.data.model.City
import com.github.eylulnc.walkmunich.core.data.model.Place
import com.github.eylulnc.walkmunich.core.data.model.SearchResult
import com.github.eylulnc.walkmunich.feature.main.data.city.repository.CityRepository
import com.github.eylulnc.walkmunich.feature.main.data.place.repository.PlacesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.Normalizer

data class MainUiState(
    val isLoading: Boolean = true,
    val city: City? = null,
    val error: String? = null,

    val searchQuery: String = "",
    val allPlaces: List<Place> = emptyList(),
    val searchResults: List<SearchResult> = emptyList(),
    val isSearching: Boolean = false
)

class MainScreenViewModel(
    private val repository: CityRepository,
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    private var searchJob: Job? = null

    init {
        loadCity()
        loadPlaces()
    }

    private fun loadCity() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            runCatching {
                repository.getCity().collect { city ->
                    _uiState.update { it.copy(isLoading = false, city = city) }
                }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun loadPlaces() {
        viewModelScope.launch {
            runCatching {
                // Supports either a suspend list or a Flow<List<PlaceMin>>
                val data = placesRepository.getPlaces() // suspend returning List<PlaceMin>
                _uiState.update { it.copy(allPlaces = data) }
                applySearch(_uiState.value.searchQuery, data)
            }.onFailure { e ->
                // Don’t fail the whole screen; just keep search empty
                _uiState.update { it.copy(error = it.error ?: e.message) }
            }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        
        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }
        
        _uiState.update { it.copy(isSearching = true) }
        searchJob = viewModelScope.launch {
            delay(150)
            applySearch(query, _uiState.value.allPlaces)
        }
    }

    fun onClearQuery() = onQueryChange("")

    private fun applySearch(query: String, data: List<Place>) {
        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList(), isSearching = false) }
            return
        }
        val q = normalize(query)

        val results = data
            .mapNotNull { p ->
                val name = normalize(p.name)
                val cat = p.category.name.lowercase()
                val matches =
                    name.startsWith(q) || name.contains(q) || cat.contains(q)
                if (!matches) return@mapNotNull null

                val score =
                    when {
                        name.startsWith(q) -> 0
                        name.contains(q) -> 1
                        else -> 2
                    } * 100 + name.length // small tiebreaker

                SearchResult(
                    place = p,
                    category = p.category
                ) to score
            }
            .sortedBy { it.second }
            .map { it.first }

        _uiState.update { it.copy(searchResults = results, isSearching = false) }
    }

    private fun normalize(s: String): String =
        Normalizer.normalize(s, Normalizer.Form.NFD)
            .replace("\\p{M}+".toRegex(), "")
            .replace("ß", "ss")
            .replace("ä", "ae").replace("ö", "oe").replace("ü", "ue")
            .lowercase()

}
