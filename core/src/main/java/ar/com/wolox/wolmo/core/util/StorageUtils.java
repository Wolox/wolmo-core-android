package ar.com.wolox.wolmo.core.util;

import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * Storage utils to query and store values in {@link SharedPreferences}.
 * TODO: Add javadoc to each method
 */
public final class StorageUtils {

    private SharedPreferences mSharedPreferences;

    @Inject
    public StorageUtils(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    /**
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
