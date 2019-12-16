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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadows.ShadowBitmap;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, shadows = {ImageProviderTest.WolmoShadowIntent.class, ImageProviderTest.WolmoShadowBitmap.class})
public class ImageProviderTest {

    private static ComponentName sComponentNameMock;
    private static Intent sIntentInstance;
    private static HashMap<String, String> sCompressionProperties = new HashMap<>();

    private Context mContextSpy;
    private WolmoFileProvider mWolmoFileProviderMock;

    private ImageProvider mImageProviderSpy;

    @Before
    public void beforeTest() {
        mContextSpy = spy(ApplicationProvider.getApplicationContext());
        mWolmoFileProviderMock = mock(WolmoFileProvider.class);

        mImageProviderSpy = new ImageProvider(mContextSpy, mWolmoFileProviderMock);
    }

    @Test
    public void getImageFromGalleryWithExistingGalleryStartsActivity() {
        Fragment fragmentMock = mock(Fragment.class);
        sComponentNameMock = mock(ComponentName.class);

        final Boolean response = mImageProviderSpy.getImageFromGallery(fragmentMock, 1);

        assertThat(response).isEqualTo(true);
        assertThat(sIntentInstance.getBooleanExtra(Intent.EXTRA_LOCAL_ONLY, false)).isEqualTo(true);
        verify(fragmentMock, times(1)).startActivityForResult(any(Intent.class), eq(1));
    }

    @Test
    public void getImageFromGalleryWithNoGalleryShouldShowToast() {
        Fragment fragmentMock = mock(Fragment.class);
        sComponentNameMock = null;

        final Boolean response = mImageProviderSpy.getImageFromGallery(fragmentMock, 1);

        assertThat(response).isEqualTo(false);
        assertThat(sIntentInstance.getBooleanExtra(Intent.EXTRA_LOCAL_ONLY, false)).isEqualTo(true);
        verify(fragmentMock, times(0)).startActivityForResult(any(Intent.class), eq(1));
    }

    @Test
    public void addImageToPictureGallerySendBroadcast() {
        Uri uriMock = mock(Uri.class);

        mImageProviderSpy.addPictureToDeviceGallery(uriMock);

        assertThat(sIntentInstance.getData()).isSameAs(uriMock);
        verify(mContextSpy, times(1)).sendBroadcast(eq(sIntentInstance));
    }


    @Test
    public void getImageFromCameraWithoutCameraShowsToast() {
        Fragment fragmentMock = mock(Fragment.class);
        sComponentNameMock = null; // No Camera app

        final Boolean response = mImageProviderSpy.getImageFromCamera(fragmentMock, 123, "/path/Filename.jpg");

        assertThat(response).isEqualTo(false);
    }

    @Test
    public void getImageFromCameraWithoutErrorStartsActivity() throws IOException {
        Fragment fragmentMock = mock(Fragment.class);
        File fileMock = mock(File.class);
        Uri uriMock = mock(Uri.class);
        sComponentNameMock = mock(ComponentName.class);

        when(fragmentMock.getContext()).thenReturn(mContextSpy);
        when(mWolmoFileProviderMock.createTempFile(anyString(), anyString(), anyString())).thenReturn(fileMock);
        when(mWolmoFileProviderMock.getUriForFile(any(File.class))).thenReturn(uriMock);

        final Boolean response = mImageProviderSpy.getImageFromCamera(fragmentMock, 123, "/file/path.jpg");

        assertThat(response).isEqualTo(true);
        assertThat(sIntentInstance.getAction()).isEqualTo(MediaStore.ACTION_IMAGE_CAPTURE);
        assertThat(sIntentInstance.getFlags()).matches(flags -> (flags & Intent.FLAG_GRANT_READ_URI_PERMISSION) == 1);
        assertThat((Uri) sIntentInstance.getParcelableExtra(MediaStore.EXTRA_OUTPUT)).isSameAs(uriMock);
    }

    @Test
    public void fitBitmapShouldScaleDownWithRatio() {
        // Scale with more width than height
        Bitmap bitmap = Bitmap.createBitmap(1000, 500, Bitmap.Config.ARGB_8888);
        Bitmap fitBitmap = ImageProvider.fit(bitmap, 500, 500);
        assertThat(fitBitmap.getHeight()).isEqualTo(250);
        assertThat(fitBitmap.getWidth()).isEqualTo(500);

        // Scale with more height than width
        bitmap = Bitmap.createBitmap(500, 1000, Bitmap.Config.ARGB_8888);
        fitBitmap = ImageProvider.fit(bitmap, 500, 500);
        assertThat(fitBitmap.getHeight()).isEqualTo(500);
        assertThat(fitBitmap.getWidth()).isEqualTo(250);
    }

    @Test
    public void getImageAsByteArray() {
        Bitmap bitmap = Bitmap.createBitmap(10, 5, Bitmap.Config.ARGB_8888);
        ImageProvider.getImageAsByteArray(bitmap, Bitmap.CompressFormat.PNG, 50, 50, 50);

        assertThat(sCompressionProperties).containsValues(Bitmap.CompressFormat.PNG.toString(), "50");
    }

    /**
     * Shadow new instances of {@link Intent}
     */
    @Implements(Intent.class)
    public static class WolmoShadowIntent {
        @RealObject private Intent realIntent;

        public void __constructor__(String action) {
            realIntent.setAction(action);
            sIntentInstance = realIntent;
        }

        public void __constructor__(String action, Uri uri) {
            __constructor__(action);
        }

        @Implementation
        public ComponentName resolveActivity(@NonNull PackageManager pm) {
            return sComponentNameMock;
        }
    }

    @Implements(Bitmap.class)
    public static class WolmoShadowBitmap extends ShadowBitmap {

        @Override
        public boolean compress(Bitmap.CompressFormat format, int quality, OutputStream stream) {
            sCompressionProperties.put("format", format.toString());
            sCompressionProperties.put("quality", String.valueOf(quality));
            return super.compress(format, quality, stream);
        }
    }
}
