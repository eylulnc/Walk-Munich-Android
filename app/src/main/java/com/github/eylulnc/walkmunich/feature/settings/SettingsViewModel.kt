package com.github.eylulnc.walkmunich.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.eylulnc.walkmunich.core.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val isDarkTheme = userPreferencesRepository.isDarkTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun setDarkTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setDarkTheme(isDarkTheme)
        }
    }

    fun clearRecentlyViewed() {
        viewModelScope.launch { userPreferencesRepository.clearRecentlyViewed() }
    }

    fun clearFavorites() {
        viewModelScope.launch { userPreferencesRepository.clearFavorites() }
    }
}