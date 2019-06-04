package ar.com.wolox.wolmo.core.util

import android.content.SharedPreferences
import ar.com.wolox.wolmo.core.di.scopes.ApplicationScope
import javax.inject.Inject

/**
 * Utility class to query and store values in {@link SharedPreferences}.
 */
@ApplicationScope
class SharedPreferencesManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    /*
     * A bunch of shared preferences utils methods to get and set different types of values
     */
    fun store(key: String, value: String) = sharedPreferences.edit().putString(key, value).apply()

    fun store(key: String, value: Int?) = sharedPreferences.edit().putInt(key, value!!).apply()

    fun store(key: String, value: Float?) = sharedPreferences.edit().putFloat(key, value!!).apply()

    fun store(key: String, value: Boolean?) = sharedPreferences.edit().putBoolean(key, value!!).apply()

    fun store(key: String, value: Long?) = sharedPreferences.edit().putLong(key, value!!).apply()

    operator fun get(key: String, defValue: String) = sharedPreferences.getString(key, defValue).orEmpty()

    operator fun get(key: String, defValue: Int) = sharedPreferences.getInt(key, defValue)

    operator fun get(key: String, defValue: Float) = sharedPreferences.getFloat(key, defValue)

    operator fun get(key: String, defValue: Boolean) = sharedPreferences.getBoolean(key, defValue)

    operator fun get(key: String, defValue: Long) = sharedPreferences.getLong(key, defValue)

    fun clearKey(key: String) = sharedPreferences.edit().remove(key).apply()

    fun keyExists(key: String) = sharedPreferences.contains(key)
}