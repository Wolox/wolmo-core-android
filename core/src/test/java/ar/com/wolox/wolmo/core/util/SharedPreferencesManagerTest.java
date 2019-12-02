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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;

import ar.com.wolox.wolmo.core.util.SharedPreferencesManager;

public class SharedPreferencesManagerTest {

    private SharedPreferences.Editor mEditorMock;
    private SharedPreferences mSharedPreferencesMock;
    private SharedPreferencesManager mSharedPreferencesManager;

    @Before
    public void beforeTest() {
        mEditorMock = mock(SharedPreferences.Editor.class);
        configEditor(mEditorMock);

        mSharedPreferencesMock = mock(SharedPreferences.class);
        when(mSharedPreferencesMock.edit()).thenReturn(mEditorMock);
        mSharedPreferencesManager = new SharedPreferencesManager(mSharedPreferencesMock);
    }

    private void configEditor(SharedPreferences.Editor editorMock) {
        when(editorMock.putBoolean(anyString(), anyBoolean())).thenReturn(editorMock);
        when(editorMock.putFloat(anyString(), anyFloat())).thenReturn(editorMock);
        when(editorMock.putInt(anyString(), anyInt())).thenReturn(editorMock);
        when(editorMock.putLong(anyString(), anyLong())).thenReturn(editorMock);
        when(editorMock.putString(anyString(), anyString())).thenReturn(editorMock);
        when(editorMock.remove(anyString())).thenReturn(editorMock);
    }

    @Test
    public void storeShouldCallEditor() {
        mSharedPreferencesManager.store("Long", 1111L);
        mSharedPreferencesManager.store("Float", 4321F);
        mSharedPreferencesManager.store("String", "Store");
        mSharedPreferencesManager.store("Boolean", true);
        mSharedPreferencesManager.store("Integer", 1234);

        verify(mEditorMock, times(5)).apply();
        verify(mEditorMock, times(1)).putLong(eq("Long"), eq(1111L));
        verify(mEditorMock, times(1)).putFloat(eq("Float"), eq(4321F));
        verify(mEditorMock, times(1)).putString(eq("String"), eq("Store"));
        verify(mEditorMock, times(1)).putBoolean(eq("Boolean"), eq(true));
        verify(mEditorMock, times(1)).putInt(eq("Integer"), eq(1234));
    }

    @Test
    public void getShouldCallSharedPreferences() {
        when(mSharedPreferencesMock.getBoolean(anyString(), anyBoolean())).thenReturn(true);
        when(mSharedPreferencesMock.getFloat(anyString(), anyFloat())).thenReturn(123F);
        when(mSharedPreferencesMock.getInt(anyString(), anyInt())).thenReturn(1234);
        when(mSharedPreferencesMock.getLong(anyString(), anyLong())).thenReturn(123456L);
        when(mSharedPreferencesMock.getString(anyString(), anyString())).thenReturn("Value");

        assertThat(mSharedPreferencesManager.get("Boolean", false)).isEqualTo(true);
        assertThat(mSharedPreferencesManager.get("Float", 0F)).isEqualTo(123F);
        assertThat(mSharedPreferencesManager.get("Int", 0)).isEqualTo(1234);
        assertThat(mSharedPreferencesManager.get("Long", 0L)).isEqualTo(123456L);
        assertThat(mSharedPreferencesManager.get("String", "")).isEqualTo("Value");
    }

    @Test
    public void clearKeyShouldCallEditor() {
        mSharedPreferencesManager.clearKey("KeyToClean");

        verify(mEditorMock, times(1)).remove(eq("KeyToClean"));
        verify(mEditorMock, times(1)).apply();
    }

    @Test
    public void keyExistsShouldCallSharedPreferences() {
        when(mSharedPreferencesMock.contains(eq("ExistingKey"))).thenReturn(true);

        assertThat(mSharedPreferencesManager.keyExists("ExistingKey")).isTrue();
        assertThat(mSharedPreferencesManager.keyExists("NotExistingKey")).isFalse();
        verify(mSharedPreferencesMock, times(2)).contains(anyString());
    }

}
