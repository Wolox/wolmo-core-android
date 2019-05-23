package ar.com.wolox.wolmo.core.util

import android.content.SharedPreferences
import ar.com.wolox.wolmo.core.di.scopes.ApplicationScope
import javax.inject.Inject

/**
 * Utility class to query and store values in {@link SharedPreferences}.
 */
@ApplicationScope
class SharedPreferencesManager @Inject constructor(private val mSharedPreferences: SharedPreferences) {

    /*
     * A bunch of shared preferences utils methods to get and set different types of values
     */
    fun store(key: String, value: String) = mSharedPreferences.edit().putString(key, value).apply()

    fun store(key: String, value: Int?) = mSharedPreferences.edit().putInt(key, value!!).apply()

    fun store(key: String, value: Float?) = mSharedPreferences.edit().putFloat(key, value!!).apply()

    fun store(key: String, value: Boolean?) = mSharedPreferences.edit().putBoolean(key, value!!).apply()

    fun store(key: String, value: Long?) = mSharedPreferences.edit().putLong(key, value!!).apply()

    operator fun get(key: String, defValue: String) = mSharedPreferences.getString(key, defValue).orEmpty()

    operator fun get(key: String, defValue: Int) = mSharedPreferences.getInt(key, defValue)

    operator fun get(key: String, defValue: Float) = mSharedPreferences.getFloat(key, defValue)

    operator fun get(key: String, defValue: Boolean) = mSharedPreferences.getBoolean(key, defValue)

    operator fun get(key: String, defValue: Long) = mSharedPreferences.getLong(key, defValue)

    fun clearKey(key: String) = mSharedPreferences.edit().remove(key).apply()

    fun keyExists(key: String) = mSharedPreferences.contains(key)
}