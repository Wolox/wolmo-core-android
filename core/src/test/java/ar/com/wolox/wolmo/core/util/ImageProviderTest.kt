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
        internal var sComponentNameMock: ComponentName? = null
        internal var sIntentInstance: Intent? = null
        internal var sCompressionProperties = HashMap<String, String>()


        /**
         * Shadow new instances of [Intent]
         */
        @Implements(Intent::class)
        class WolmoShadowIntent {
            @RealObject
            private val realIntent: Intent? = null

            fun __constructor__(action: String) {
                realIntent?.action = action
                sIntentInstance = realIntent
            }

            fun __constructor__(action: String, uri: Uri) {
                __constructor__(action)
            }

            @Implementation
            fun resolveActivity(pm: PackageManager): ComponentName? {
                return sComponentNameMock
            }
        }

        @Implements(Bitmap::class)
        class WolmoShadowBitmap : ShadowBitmap() {

            public override fun compress(format: Bitmap.CompressFormat, quality: Int, stream: OutputStream): Boolean {
                sCompressionProperties["format"] = format.toString()
                sCompressionProperties["quality"] = quality.toString()
                return super.compress(format, quality, stream)
            }
        }
    }

    private var mContextSpy: Context? = null
    private var mToastFactoryMock: ToastFactory? = null
    private var mWolmoFileProviderMock: WolmoFileProvider? = null

    private var mImageProviderSpy: ImageProvider? = null

    @Before
    fun beforeTest() {
        mContextSpy = spy<Application>(RuntimeEnvironment.application)
        mToastFactoryMock = mock<ToastFactory>(ToastFactory::class.java)
        mWolmoFileProviderMock = mock<WolmoFileProvider>(WolmoFileProvider::class.java)

        mImageProviderSpy = ImageProvider(mContextSpy as Application, mToastFactoryMock as ToastFactory, mWolmoFileProviderMock as WolmoFileProvider)
    }

    @Test
    fun getImageFromGalleryWithExistingGalleryStartsActivity() {
        val fragmentMock = mock(Fragment::class.java)
        sComponentNameMock = mock(ComponentName::class.java)

        mImageProviderSpy?.getImageFromGallery(fragmentMock, 1, 123)

        assertThat(sIntentInstance?.getBooleanExtra(Intent.EXTRA_LOCAL_ONLY, false)).isEqualTo(true)
        verify<Fragment>(fragmentMock, times(1)).startActivityForResult(any<Intent>(Intent::class.java), eq(1))
        verify<ToastFactory>(mToastFactoryMock, times(0)).show(eq(123))
    }

    @Test
    fun getImageFromGalleryWithNoGalleryShouldShowToast() {
        val fragmentMock = mock(Fragment::class.java)
        sComponentNameMock = null

        mImageProviderSpy?.getImageFromGallery(fragmentMock, 1, 123)

        assertThat(sIntentInstance?.getBooleanExtra(Intent.EXTRA_LOCAL_ONLY, false)).isEqualTo(true)
        verify(fragmentMock, times(0)).startActivityForResult(any(Intent::class.java), eq(1))
        verify<ToastFactory>(mToastFactoryMock, times(1)).show(eq(123))
    }

    @Test
    fun addImageToPictureGallerySendBroadcast() {
        val uriMock = mock(Uri::class.java)

        mImageProviderSpy?.addPictureToDeviceGallery(uriMock)

        assertThat(sIntentInstance?.data).isSameAs(uriMock)
        verify<Context>(mContextSpy, times(1)).sendBroadcast(eq<Intent>(sIntentInstance))
    }

    @Test
    fun getImageFromCameraWithoutCameraShowsToast() {
        val fragmentMock = mock(Fragment::class.java)
        sComponentNameMock = null // No Camera app

        mImageProviderSpy?.getImageFromCamera(fragmentMock, 123, "Filename", "JPG", 12345)

        verify<ToastFactory>(mToastFactoryMock, times(1)).show(eq(12345))
    }

    @Test
    @Throws(IOException::class)
    fun getImageFromCameraWithExceptionShowsToast() {
        val fragmentMock = mock(Fragment::class.java)
        sComponentNameMock = mock(ComponentName::class.java)
        `when`(mWolmoFileProviderMock?.createTempFile(anyString(), anyString(), anyString())).thenThrow(IOException())

        assertThat(mImageProviderSpy?.getImageFromCamera(fragmentMock, 123, "Filename", "JPG", 123456)).isNull()
        verify<ToastFactory>(mToastFactoryMock, times(1)).show(eq(123456))
    }

    @Test
    @Throws(IOException::class)
    fun getImageFromCameraWithoutErrorStartsActivity() {
        val fragmentMock = mock(Fragment::class.java)
        val fileMock = mock(File::class.java)
        val uriMock = mock(Uri::class.java)
        sComponentNameMock = mock(ComponentName::class.java)

        `when`<Context>(fragmentMock.context).thenReturn(mContextSpy)
        `when`(mWolmoFileProviderMock?.createTempFile(anyString(), anyString(), anyString())).thenReturn(fileMock)
        `when`(mWolmoFileProviderMock?.getUriForFile(fileMock)).thenReturn(uriMock)


        assertThat(mImageProviderSpy?.getImageFromCamera(fragmentMock, 123, "Filename", "JPG", 123456)).isSameAs(fileMock)
        assertThat(sIntentInstance?.action).isEqualTo(MediaStore.ACTION_IMAGE_CAPTURE)
        assertThat(sIntentInstance?.flags).matches { flags -> flags and Intent.FLAG_GRANT_READ_URI_PERMISSION == 1 }
        assertThat(sIntentInstance?.getParcelableExtra(MediaStore.EXTRA_OUTPUT) as Uri).isSameAs(uriMock)

        verify<ToastFactory>(mToastFactoryMock, times(0)).show(anyInt())
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

        assertThat(sCompressionProperties).containsValues(Bitmap.CompressFormat.PNG.toString(), "50")
    }
}