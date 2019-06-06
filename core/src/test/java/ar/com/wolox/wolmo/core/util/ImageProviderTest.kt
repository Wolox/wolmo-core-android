package ar.com.wolox.wolmo.core.util

import org.assertj.core.api.Assertions.assertThat
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.annotation.RealObject
import org.robolectric.shadows.ShadowBitmap
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.util.HashMap

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, shadows = [ImageProviderTest.Companion.WolmoShadowIntent::class, ImageProviderTest.Companion.WolmoShadowBitmap::class])
class ImageProviderTest {

    companion object {
        internal var componentNameMock: ComponentName? = null
        internal var intentInstance: Intent? = null
        internal var compressionProperties = HashMap<String, String>()


        /**
         * Shadow new instances of [Intent]
         */
        @Implements(Intent::class)
        class WolmoShadowIntent {
            @RealObject
            private val realIntent: Intent? = null

            fun __constructor__(action: String) {
                realIntent?.action = action
                intentInstance = realIntent
            }

            fun __constructor__(action: String, uri: Uri) {
                __constructor__(action)
            }

            @Implementation
            fun resolveActivity(pm: PackageManager): ComponentName? {
                return componentNameMock
            }
        }

        @Implements(Bitmap::class)
        class WolmoShadowBitmap : ShadowBitmap() {

            public override fun compress(format: Bitmap.CompressFormat, quality: Int, stream: OutputStream): Boolean {
                compressionProperties["format"] = format.toString()
                compressionProperties["quality"] = quality.toString()
                return super.compress(format, quality, stream)
            }
        }
    }

    private var contextSpy: Context? = null
    private var toastFactoryMock: ToastFactory? = null
    private var wolmoFileProviderMock: WolmoFileProvider? = null

    private var imageProviderSpy: ImageProvider? = null

    @Before
    fun beforeTest() {
        contextSpy = spy<Application>(RuntimeEnvironment.application)
        toastFactoryMock = mock<ToastFactory>(ToastFactory::class.java)
        wolmoFileProviderMock = mock<WolmoFileProvider>(WolmoFileProvider::class.java)

        imageProviderSpy = ImageProvider(contextSpy as Application, toastFactoryMock as ToastFactory, wolmoFileProviderMock as WolmoFileProvider)
    }

    @Test
    fun getImageFromGalleryWithExistingGalleryStartsActivity() {
        val fragmentMock = mock(Fragment::class.java)
        componentNameMock = mock(ComponentName::class.java)

        imageProviderSpy?.getImageFromGallery(fragmentMock, 1, 123)

        assertThat(intentInstance?.getBooleanExtra(Intent.EXTRA_LOCAL_ONLY, false)).isEqualTo(true)
        verify<Fragment>(fragmentMock, times(1)).startActivityForResult(any<Intent>(Intent::class.java), eq(1))
        verify<ToastFactory>(toastFactoryMock, times(0)).show(eq(123))
    }

    @Test
    fun getImageFromGalleryWithNoGalleryShouldShowToast() {
        val fragmentMock = mock(Fragment::class.java)
        componentNameMock = null

        imageProviderSpy?.getImageFromGallery(fragmentMock, 1, 123)

        assertThat(intentInstance?.getBooleanExtra(Intent.EXTRA_LOCAL_ONLY, false)).isEqualTo(true)
        verify(fragmentMock, times(0)).startActivityForResult(any(Intent::class.java), eq(1))
        verify<ToastFactory>(toastFactoryMock, times(1)).show(eq(123))
    }

    @Test
    fun addImageToPictureGallerySendBroadcast() {
        val uriMock = mock(Uri::class.java)

        imageProviderSpy?.addPictureToDeviceGallery(uriMock)

        assertThat(intentInstance?.data).isSameAs(uriMock)
        verify<Context>(contextSpy, times(1)).sendBroadcast(eq<Intent>(intentInstance))
    }

    @Test
    fun getImageFromCameraWithoutCameraShowsToast() {
        val fragmentMock = mock(Fragment::class.java)
        componentNameMock = null // No Camera app

        imageProviderSpy?.getImageFromCamera(fragmentMock, 123, "Filename", "JPG", 12345)

        verify<ToastFactory>(toastFactoryMock, times(1)).show(eq(12345))
    }

    @Test
    @Throws(IOException::class)
    fun getImageFromCameraWithExceptionShowsToast() {
        val fragmentMock = mock(Fragment::class.java)
        componentNameMock = mock(ComponentName::class.java)
        `when`(wolmoFileProviderMock?.createTempFile(anyString(), anyString(), anyString())).thenThrow(IOException())

        assertThat(imageProviderSpy?.getImageFromCamera(fragmentMock, 123, "Filename", "JPG", 123456)).isNull()
        verify<ToastFactory>(toastFactoryMock, times(1)).show(eq(123456))
    }

    @Test
    @Throws(IOException::class)
    fun getImageFromCameraWithoutErrorStartsActivity() {
        val fragmentMock = mock(Fragment::class.java)
        val fileMock = mock(File::class.java)
        val uriMock = mock(Uri::class.java)
        componentNameMock = mock(ComponentName::class.java)

        `when`<Context>(fragmentMock.context).thenReturn(contextSpy)
        `when`(wolmoFileProviderMock?.createTempFile(anyString(), anyString(), anyString())).thenReturn(fileMock)
        `when`(wolmoFileProviderMock?.getUriForFile(fileMock)).thenReturn(uriMock)


        assertThat(imageProviderSpy?.getImageFromCamera(fragmentMock, 123, "Filename", "JPG", 123456)).isSameAs(fileMock)
        assertThat(intentInstance?.action).isEqualTo(MediaStore.ACTION_IMAGE_CAPTURE)
        assertThat(intentInstance?.flags).matches { flags -> flags and Intent.FLAG_GRANT_READ_URI_PERMISSION == 1 }
        assertThat(intentInstance?.getParcelableExtra(MediaStore.EXTRA_OUTPUT) as Uri).isSameAs(uriMock)

        verify<ToastFactory>(toastFactoryMock, times(0)).show(anyInt())
    }

    @Test
    fun fitBitmapShouldScaleDownWithRatio() {
        // Scale with more width than height
        var bitmap = Bitmap.createBitmap(1000, 500, Bitmap.Config.ARGB_8888)
        var fitBitmap = ImageProvider.fit(bitmap, 500, 500)
        assertThat(fitBitmap.height).isEqualTo(250)
        assertThat(fitBitmap.width).isEqualTo(500)

        // Scale with more height than width
        bitmap = Bitmap.createBitmap(500, 1000, Bitmap.Config.ARGB_8888)
        fitBitmap = ImageProvider.fit(bitmap, 500, 500)
        assertThat(fitBitmap.height).isEqualTo(500)
        assertThat(fitBitmap.width).isEqualTo(250)
    }

    @Test
    fun getImageAsByteArray() {
        val bitmap = Bitmap.createBitmap(10, 5, Bitmap.Config.ARGB_8888)
        ImageProvider.getImageAsByteArray(bitmap, Bitmap.CompressFormat.PNG, 50, 50, 50)

        assertThat(compressionProperties).containsValues(Bitmap.CompressFormat.PNG.toString(), "50")
    }
}