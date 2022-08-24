package ar.com.wolox.wolmo.core.util

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("preferences")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {
    private val settingsDataStore = appContext.dataStore

    suspend fun <T> getPreference(
        PreferencesKey: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> =
        settingsDataStore.data.catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            val result = it[PreferencesKey] ?: defaultValue
            result
        }

    suspend fun <T> savePreference(key: Preferences.Key<T>, value: T) {
        settingsDataStore.edit {
            it[key] = value
        }
    }

    suspend fun <T> removePreference(key: Preferences.Key<T>) {
        settingsDataStore.edit {
            it.remove(key)
        }
    }

    suspend fun clearAllPreference() {
        settingsDataStore.edit {
            it.clear()
        }
    }

}