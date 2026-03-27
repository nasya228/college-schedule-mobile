package com.example.collegeschedule.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("com/example/collegeschedule/ui/theme/favorites")

class FavoritesDataStore(private val context: Context) {
    private val FAVORITES_KEY = stringSetPreferencesKey("favorite_groups")

    val favoritesFlow: Flow<Set<String>> = context.dataStore.data
        .map { preferences -> preferences[FAVORITES_KEY] ?: emptySet() }

    suspend fun addFavorite(group: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[FAVORITES_KEY] ?: emptySet()
            preferences[FAVORITES_KEY] = current + group
        }
    }

    suspend fun removeFavorite(group: String) {
        context.dataStore.edit { preferences ->
            val current = preferences[FAVORITES_KEY] ?: emptySet()
            preferences[FAVORITES_KEY] = current - group
        }
    }
}