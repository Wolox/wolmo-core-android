package ar.com.wolox.wolmo.core.util

import android.content.SharedPreferences
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyFloat
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class SharedPreferencesManagerTest {

    private var mEditorMock: SharedPreferences.Editor? = null
    private var mSharedPreferencesMock: SharedPreferences? = null
    private var mSharedPreferencesManager: SharedPreferencesManager? = null

    @Before
    fun beforeTest() {
        mEditorMock = mock<SharedPreferences.Editor>(SharedPreferences.Editor::class.java)
        configEditor(mEditorMock)

        mSharedPreferencesMock = mock<SharedPreferences>(SharedPreferences::class.java)
        `when`(mSharedPreferencesMock?.edit()).thenReturn(mEditorMock)
        mSharedPreferencesMock?.let { mSharedPreferencesManager = SharedPreferencesManager(it) }
    }

    private fun configEditor(editorMock: SharedPreferences.Editor?) {
        `when`(editorMock?.putBoolean(anyString(), anyBoolean())).thenReturn(editorMock)
        `when`(editorMock?.putFloat(anyString(), anyFloat())).thenReturn(editorMock)
        `when`(editorMock?.putInt(anyString(), anyInt())).thenReturn(editorMock)
        `when`(editorMock?.putLong(anyString(), anyLong())).thenReturn(editorMock)
        `when`(editorMock?.putString(anyString(), anyString())).thenReturn(editorMock)
        `when`(editorMock?.remove(anyString())).thenReturn(editorMock)
    }

    @Test
    fun storeShouldCallEditor() {
        mSharedPreferencesManager?.store("Long", 1111L)
        mSharedPreferencesManager?.store("Float", 4321f)
        mSharedPreferencesManager?.store("String", "Store")
        mSharedPreferencesManager?.store("Boolean", true)
        mSharedPreferencesManager?.store("Integer", Integer.valueOf(1234))

        verify(mEditorMock, times(5))?.apply()
        verify(mEditorMock, times(1))?.putLong(eq("Long"), eq(1111L))
        verify(mEditorMock, times(1))?.putFloat(eq("Float"), eq(4321f))
        verify(mEditorMock, times(1))?.putString(eq("String"), eq("Store"))
        verify(mEditorMock, times(1))?.putBoolean(eq("Boolean"), eq(true))
        verify(mEditorMock, times(1))?.putInt(eq("Integer"), eq(1234))
    }

    @Test
    fun getShouldCallSharedPreferences() {
        `when`(mSharedPreferencesMock?.getBoolean(anyString(), anyBoolean())).thenReturn(true)
        `when`(mSharedPreferencesMock?.getFloat(anyString(), anyFloat())).thenReturn(123f)
        `when`(mSharedPreferencesMock?.getInt(anyString(), anyInt())).thenReturn(1234)
        `when`(mSharedPreferencesMock?.getLong(anyString(), anyLong())).thenReturn(123456L)
        `when`(mSharedPreferencesMock?.getString(anyString(), anyString())).thenReturn("Value")

        assertThat(mSharedPreferencesManager?.get("Boolean", false)).isEqualTo(true)
        assertThat(mSharedPreferencesManager?.get("Float", 0f)).isEqualTo(123f)
        assertThat(mSharedPreferencesManager?.get("Int", 0)).isEqualTo(1234)
        assertThat(mSharedPreferencesManager?.get("Long", 0L)).isEqualTo(123456L)
        assertThat(mSharedPreferencesManager?.get("String", "")).isEqualTo("Value")
    }

    @Test
    fun clearKeyShouldCallEditor() {
        mSharedPreferencesManager?.clearKey("KeyToClean")

        verify(mEditorMock, times(1))?.remove(eq("KeyToClean"))
        verify(mEditorMock, times(1))?.apply()
    }

    @Test
    fun keyExistsShouldCallSharedPreferences() {
        `when`(mSharedPreferencesMock?.contains(eq("ExistingKey"))).thenReturn(true)

        assertThat(mSharedPreferencesManager?.keyExists("ExistingKey")).isTrue()
        assertThat(mSharedPreferencesManager?.keyExists("NotExistingKey")).isFalse()
        verify(mSharedPreferencesMock, times(2))?.contains(anyString())
    }
}