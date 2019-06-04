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

    private var editorMock: SharedPreferences.Editor? = null
    private var sharedPreferencesMock: SharedPreferences? = null
    private var sharedPreferencesManager: SharedPreferencesManager? = null

    @Before
    fun beforeTest() {
        editorMock = mock<SharedPreferences.Editor>(SharedPreferences.Editor::class.java)
        configEditor(editorMock)

        sharedPreferencesMock = mock<SharedPreferences>(SharedPreferences::class.java)
        `when`(sharedPreferencesMock?.edit()).thenReturn(editorMock)
        sharedPreferencesMock?.let { sharedPreferencesManager = SharedPreferencesManager(it) }
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
        sharedPreferencesManager?.store("Long", 1111L)
        sharedPreferencesManager?.store("Float", 4321f)
        sharedPreferencesManager?.store("String", "Store")
        sharedPreferencesManager?.store("Boolean", true)
        sharedPreferencesManager?.store("Integer", Integer.valueOf(1234))

        verify(editorMock, times(5))?.apply()
        verify(editorMock, times(1))?.putLong(eq("Long"), eq(1111L))
        verify(editorMock, times(1))?.putFloat(eq("Float"), eq(4321f))
        verify(editorMock, times(1))?.putString(eq("String"), eq("Store"))
        verify(editorMock, times(1))?.putBoolean(eq("Boolean"), eq(true))
        verify(editorMock, times(1))?.putInt(eq("Integer"), eq(1234))
    }

    @Test
    fun getShouldCallSharedPreferences() {
        `when`(sharedPreferencesMock?.getBoolean(anyString(), anyBoolean())).thenReturn(true)
        `when`(sharedPreferencesMock?.getFloat(anyString(), anyFloat())).thenReturn(123f)
        `when`(sharedPreferencesMock?.getInt(anyString(), anyInt())).thenReturn(1234)
        `when`(sharedPreferencesMock?.getLong(anyString(), anyLong())).thenReturn(123456L)
        `when`(sharedPreferencesMock?.getString(anyString(), anyString())).thenReturn("Value")

        assertThat(sharedPreferencesManager?.get("Boolean", false)).isEqualTo(true)
        assertThat(sharedPreferencesManager?.get("Float", 0f)).isEqualTo(123f)
        assertThat(sharedPreferencesManager?.get("Int", 0)).isEqualTo(1234)
        assertThat(sharedPreferencesManager?.get("Long", 0L)).isEqualTo(123456L)
        assertThat(sharedPreferencesManager?.get("String", "")).isEqualTo("Value")
    }

    @Test
    fun clearKeyShouldCallEditor() {
        sharedPreferencesManager?.clearKey("KeyToClean")

        verify(editorMock, times(1))?.remove(eq("KeyToClean"))
        verify(editorMock, times(1))?.apply()
    }

    @Test
    fun keyExistsShouldCallSharedPreferences() {
        `when`(sharedPreferencesMock?.contains(eq("ExistingKey"))).thenReturn(true)

        assertThat(sharedPreferencesManager?.keyExists("ExistingKey")).isTrue()
        assertThat(sharedPreferencesManager?.keyExists("NotExistingKey")).isFalse()
        verify(sharedPreferencesMock, times(2))?.contains(anyString())
    }
}