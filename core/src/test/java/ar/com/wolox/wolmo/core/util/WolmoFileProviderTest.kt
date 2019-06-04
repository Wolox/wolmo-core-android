package ar.com.wolox.wolmo.core.util

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.test.core.app.ApplicationProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.spy
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import java.io.File
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.LOLLIPOP], shadows = [WolmoFileProviderTest.Companion.ShadowFileProvider::class])
class WolmoFileProviderTest {

    private var contextSpy: Context? = null
    private var wolmoFileProvider: WolmoFileProvider? = null

    companion object {
        @Implements(FileProvider::class)
        object ShadowFileProvider {
             var sContext: Context? = null
             var sAuthority: String? = null
             var sFile: File? = null

            @JvmStatic
            @Implementation
            fun getUriForFile(context: Context, authority: String, file: File): Uri {
                sContext = context
                sAuthority = authority
                sFile = file
                return Uri.EMPTY
            }
        }
    }

    @Before
    fun beforeTest() {
        contextSpy = spy(ApplicationProvider.getApplicationContext<Context>())
        contextSpy?.let { wolmoFileProvider = WolmoFileProvider(it) }
    }

    @Test
    @Throws(IOException::class)
    fun createTempFileShouldCreateNewFiles() {
        wolmoFileProvider?.createTempFile("TestFile", "txt", Environment.DIRECTORY_DCIM)
        wolmoFileProvider?.createTempFile("WithDot", ".txt", Environment.DIRECTORY_DCIM)

        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        assertThat(storageDir.listFiles()).anyMatch { file -> file.name.matches("TestFile.*\\.txt".toRegex()) }
        assertThat(storageDir.listFiles()).anyMatch { file -> file.name.matches("WithDot.*\\.txt".toRegex()) }
    }

    @Test
    @Throws(IOException::class)
    fun getUriForFileShouldReturnExistingUri() {
        val file = File.createTempFile("getUri", ".txt", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))

        // Check that we are calling FileProvider

        assertThat(wolmoFileProvider?.getUriForFile(file)).isEqualTo(Uri.EMPTY)
        assertThat(ShadowFileProvider.sContext).isSameAs(contextSpy)
        assertThat(ShadowFileProvider.sAuthority).isEqualTo("ar.com.wolox.wolmo.core.test.provider")
        assertThat(ShadowFileProvider.sFile).isSameAs(file)
    }

    @Test
    fun getRealPathFromUriShouldReturnExistingPath() {
        assertThat(wolmoFileProvider?.getRealPathFromUri(Uri.EMPTY)).isNull()
    }
}