package es.upsa.mimo.gytrcompose.viewModel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import es.upsa.mimo.gytrcompose.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsViewModel(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        private val WEIGHT_KEY = stringPreferencesKey("weight")
        private val TIMER_KEY = booleanPreferencesKey("timer")
        private val DURATION_KEY = stringPreferencesKey("duration")
        private val NOTIFICATION_KEY = booleanPreferencesKey("notification")
        private val SETS_KEY = intPreferencesKey("sets")
    }

    val getWeight: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[WEIGHT_KEY] ?: context.resources.getStringArray(R.array.weight_settings_options)[0]
    }

    suspend fun saveWeight(weight: String) {
        context.dataStore.edit { preferences ->
            preferences[WEIGHT_KEY] = weight
        }
    }

    val getTimerEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[TIMER_KEY] ?: true
    }

    suspend fun saveTimerEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[TIMER_KEY] = enabled
        }
    }

    val getDuration: Flow<String> = context.dataStore.data.map { prefereces ->
        prefereces[DURATION_KEY] ?: context.resources.getStringArray(R.array.duration_settings_options)[1]
    }

    suspend fun saveDuration(duration: String) {
        context.dataStore.edit { preferences ->
            preferences[DURATION_KEY] = duration
        }
    }

    val getNotificationEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATION_KEY] ?: true
    }

    suspend fun saveNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_KEY] = enabled
        }
    }

    val getSets: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[SETS_KEY] ?: 4
    }

    suspend fun saveSets(sets: Int) {
        context.dataStore.edit { preferences ->
            preferences[SETS_KEY] = sets
        }
    }
}