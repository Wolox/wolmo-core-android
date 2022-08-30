/*
 * Copyright (c) Wolox S.A
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ar.com.wolox.wolmo.core.util

import android.content.SharedPreferences

import javax.inject.Inject

/**
 * Utility class to query and store values in [SharedPreferences].
 */
class SharedPreferencesManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun store(key: String, value: String?) = sharedPreferences.edit().putString(key, value).apply()

    fun store(key: String, value: Int) = sharedPreferences.edit().putInt(key, value).apply()

    fun store(key: String, value: Float) = sharedPreferences.edit().putFloat(key, value).apply()

    fun store(key: String, value: Boolean) = sharedPreferences.edit().putBoolean(key, value).apply()

    fun store(key: String, value: Long) = sharedPreferences.edit().putLong(key, value).apply()

    operator fun get(key: String, defValue: String?): String? = sharedPreferences.getString(key, defValue)

    operator fun get(key: String, defValue: Int): Int = sharedPreferences.getInt(key, defValue)

    operator fun get(key: String, defValue: Float): Float = sharedPreferences.getFloat(key, defValue)

    operator fun get(key: String, defValue: Boolean): Boolean = sharedPreferences.getBoolean(key, defValue)

    operator fun get(key: String, defValue: Long): Long = sharedPreferences.getLong(key, defValue)

    fun clearKey(key: String) = sharedPreferences.edit().remove(key).apply()

    fun keyExists(key: String): Boolean = sharedPreferences.contains(key)

}
