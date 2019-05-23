package ar.com.wolox.wolmo.core.util

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.widget.Toast
import androidx.test.core.app.ApplicationProvider
import ar.com.wolox.wolmo.core.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.LOLLIPOP])
class ToastFactoryTest {

    private var mContextSpy: Context? = null
    private var mToastFactory: ToastFactory? = null

    @Before
    fun beforeTest() {
        mContextSpy = spy<Context>(ApplicationProvider.getApplicationContext<Context>())
        mContextSpy?.let { mToastFactory = ToastFactory(it) }

        val resourcesMock = mock<Resources>(Resources::class.java)
        `when`(resourcesMock.getString(anyInt())).thenReturn("MockString")
        `when`(mContextSpy?.resources).thenReturn(resourcesMock)
    }

    @Test
    fun showShouldShowAShortToast() {
        mToastFactory?.show("ShowToast")
        assertThat(ShadowToast.getLatestToast().duration).isEqualTo(Toast.LENGTH_SHORT)
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("ShowToast")

        mToastFactory?.show(R.string.unknown_error)
        assertThat(ShadowToast.getLatestToast().duration).isEqualTo(Toast.LENGTH_SHORT)
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("MockString")
    }

    @Test
    fun showLongShouldShowALongToast() {
        mToastFactory?.showLong("ShowToast")
        assertThat(ShadowToast.getLatestToast().duration).isEqualTo(Toast.LENGTH_LONG)
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("ShowToast")

        mToastFactory?.showLong(R.string.unknown_error)
        assertThat(ShadowToast.getLatestToast().duration).isEqualTo(Toast.LENGTH_LONG)
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("MockString")
    }
}