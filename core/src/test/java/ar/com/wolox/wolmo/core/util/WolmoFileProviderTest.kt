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

    private lateinit var contextSpy: Context
    private lateinit var wolmoFileProvider: WolmoFileProvider

    companion object {
        @Implements(FileProvider::class)
        object ShadowFileProvider {
             var context: Context? = null
             var authority: String? = null
             var file: File? = null

            @JvmStatic
            @Implementation
            fun getUriForFile(context: Context, authority: String, file: File): Uri {
                this.context = context
                this.authority = authority
                this.file = file
                return Uri.EMPTY
            }
        }
    }

    @Before
    fun beforeTest() {
        contextSpy = spy(ApplicationProvider.getApplicationContext<Context>())
        wolmoFileProvider = WolmoFileProvider(contextSpy)
    }

    @Test
    @Throws(IOException::class)
    fun createTempFileShouldCreateNewFiles() {
        wolmoFileProvider.createTempFile("TestFile", "txt", Environment.DIRECTORY_DCIM)
        wolmoFileProvider.createTempFile("WithDot", ".txt", Environment.DIRECTORY_DCIM)

        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        assertThat(storageDir.listFiles()).anyMatch { file -> file.name.matches("TestFile.*\\.txt".toRegex()) }
        assertThat(storageDir.listFiles()).anyMatch { file -> file.name.matches("WithDot.*\\.txt".toRegex()) }
    }

    @Test
    @Throws(IOException::class)
    fun getUriForFileShouldReturnExistingUri() {
        val file = File.createTempFile("getUri", ".txt", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))

        // Check that we are calling FileProvider

        assertThat(wolmoFileProvider.getUriForFile(file)).isEqualTo(Uri.EMPTY)
        assertThat(ShadowFileProvider.context).isSameAs(contextSpy)
        assertThat(ShadowFileProvider.authority).isEqualTo("ar.com.wolox.wolmo.core.test.provider")
        assertThat(ShadowFileProvider.file).isSameAs(file)
    }

    @Test
    fun getRealPathFromUriShouldReturnExistingPath() {
        assertThat(wolmoFileProvider.getRealPathFromUri(Uri.EMPTY)).isNull()
    }
}