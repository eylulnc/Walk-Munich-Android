package com.github.eylulnc.walkmunich.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferencesRepository(private val context: Context) {

    private val isDarkThemeKey = booleanPreferencesKey("is_dark_theme")
    private val favoritePlacesKey = stringSetPreferencesKey("favorite_places")
    private val recentlyViewedKey = stringPreferencesKey("recently_viewed_places")
    private val isGridViewKey = booleanPreferencesKey("is_grid_view")
    private val hasSeenDisclaimerKey = booleanPreferencesKey("has_seen_disclaimer")

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[isDarkThemeKey] ?: false
        }

    val favoritePlaceIds: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[favoritePlacesKey] ?: emptySet()
        }

    val recentlyViewedPlaceIds: Flow<List<String>> = context.dataStore.data
        .map { preferences ->
            preferences[recentlyViewedKey]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
        }

    val isGridView: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[isGridViewKey] ?: true
        }

    val hasSeenDisclaimer: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[hasSeenDisclaimerKey] ?: false
        }

    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[isDarkThemeKey] = isDarkTheme
        }
    }

    suspend fun toggleFavorite(placeId: String) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[favoritePlacesKey] ?: emptySet()
            val newFavorites = if (currentFavorites.contains(placeId)) {
                currentFavorites - placeId
            } else {
                currentFavorites + placeId
            }
            preferences[favoritePlacesKey] = newFavorites
        }
    }

    suspend fun addToRecentlyViewed(placeId: String) {
        context.dataStore.edit { preferences ->
            val currentList = preferences[recentlyViewedKey]?.split(",")?.filter { it.isNotEmpty() }?.toMutableList() ?: mutableListOf()

            currentList.remove(placeId)
            currentList.add(0, placeId)

            val limitedList = currentList.take(6)

            preferences[recentlyViewedKey] = limitedList.joinToString(",")
        }
    }

    suspend fun setGridView(isGridView: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[isGridViewKey] = isGridView
        }
    }

    suspend fun setHasSeenDisclaimer(hasSeen: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[hasSeenDisclaimerKey] = hasSeen
        }
    }
}
