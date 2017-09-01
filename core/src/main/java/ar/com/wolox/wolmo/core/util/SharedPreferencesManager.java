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
package ar.com.wolox.wolmo.core.util;

import android.content.SharedPreferences;

import ar.com.wolox.wolmo.core.di.scopes.ApplicationScope;

import javax.inject.Inject;

/**
 * Utility class to query and store values in {@link SharedPreferences}.
 */
@ApplicationScope
public class SharedPreferencesManager {

    private SharedPreferences mSharedPreferences;

    @Inject
    public SharedPreferencesManager(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    /*
     * A bunch of shared preferences utils methods to get and set different types of values
     */
    public final void store(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    public final void store(String key, Integer value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public final void store(String key, Float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    public final void store(String key, Boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public final void store(String key, Long value) {
        mSharedPreferences.edit().putLong(key, value).apply();
    }

    public final String get(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public final Integer get(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public final Float get(String key, float defValue) {
        return mSharedPreferences.getFloat(key, defValue);
    }

    public final Boolean get(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public final Long get(String key, long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public final void clearKey(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    public final boolean keyExists(String key) {
        return mSharedPreferences.contains(key);
    }

}
