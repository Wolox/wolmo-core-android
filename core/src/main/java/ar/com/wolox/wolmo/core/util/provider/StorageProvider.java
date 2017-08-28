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
package ar.com.wolox.wolmo.core.util.provider;

import android.content.SharedPreferences;

import ar.com.wolox.wolmo.core.di.scopes.ApplicationScope;

import javax.inject.Inject;

/**
 * Storage utils to query and store values in {@link SharedPreferences}.
 */
@ApplicationScope
public class StorageProvider {

    private SharedPreferences mSharedPreferences;

    @Inject
    public StorageProvider(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    /*
     * A bunch of shared preferences utils methods to get and set different types of values
     */
    public void storeInSharedPreferences(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    public void storeInSharedPreferences(String key, Integer value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public void storeInSharedPreferences(String key, Float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    public void storeInSharedPreferences(String key, Boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public void storeInSharedPreferences(String key, Long value) {
        mSharedPreferences.edit().putLong(key, value).apply();
    }

    public String getStringFromSharedPreferences(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public Integer getIntFromSharedPreferences(String key, Integer defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public Float getFloatFromSharedPreferences(String key, Float defValue) {
        return mSharedPreferences.getFloat(key, defValue);
    }

    public Boolean getBooleanFromSharedPreferences(String key, Boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public Long getLongFromSharedPreferences(String key, Long defValue) {
        return mSharedPreferences.getLong(key, defValue);
    }

    public void clearKey(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    public boolean keyExists(String key) {
        return mSharedPreferences.contains(key);
    }

}
