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
package ar.com.wolox.wolmo.core.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;

import ar.com.wolox.wolmo.core.permission.PermissionListener;
import ar.com.wolox.wolmo.core.permission.PermissionManager;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;
import ar.com.wolox.wolmo.core.util.ImageProvider;
import ar.com.wolox.wolmo.core.util.WolmoFileProvider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = Build.VERSION_CODES.LOLLIPOP)
public class GetImageFragmentTest {

    // This codes are from GetImageFragment
    private static final int INTENT_CODE_IMAGE_GALLERY = 9000;
    private static final int INTENT_CODE_IMAGE_CAMERA = 9001;

    private static final String[] CAMERA_PERMISSIONS = new String[] {
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    static final int GALLERY_ERROR_RES_ID = 123;
    static final int CAMERA_ERROR_RES_ID = 1234;
    static final String CAMERA_FILENAME = "Filename";

    private WolmoFragmentHandler<Object, BasePresenter<Object>> mWolmoFragmentHandlerMock;
    private PermissionManager mPermissionManagerMock;
    private ImageProvider mImageProviderMock;
    private WolmoFileProvider mWolmoFileProviderMock;
    private GetImageFragment mGetImageFragmentSpy;

    @Before
    @SuppressWarnings("unchecked")
    public void beforeTest() {
        mWolmoFragmentHandlerMock = mock(WolmoFragmentHandler.class);
        mPermissionManagerMock = mock(PermissionManager.class);
        mImageProviderMock = mock(ImageProvider.class);
        mWolmoFileProviderMock = mock(WolmoFileProvider.class);

        mGetImageFragmentSpy = spy(new TestImageFragment());
        mGetImageFragmentSpy.fragmentHandler = mWolmoFragmentHandlerMock;
        mGetImageFragmentSpy.permissionManager = mPermissionManagerMock;
        mGetImageFragmentSpy.mImageProvider = mImageProviderMock;
        mGetImageFragmentSpy.mWolmoFileProvider = mWolmoFileProviderMock;
    }


    @Test
    public void selectImageFromGallerySuccessShouldCallDependencies() {
        GetImageFragment.OnImageReturnCallback callbackMock = mock(GetImageFragment.OnImageReturnCallback.class);

        doAnswer(invocation -> {
            ((PermissionListener) invocation.getArgument(1)).onPermissionsGranted();
            return null;
        }).when(mPermissionManagerMock).requestPermission(any(Fragment.class), any(PermissionListener.class), eq(Manifest.permission.READ_EXTERNAL_STORAGE));

        mGetImageFragmentSpy.selectImageFromGallery(callbackMock);

        verify(mPermissionManagerMock, times(1)).requestPermission(eq(mGetImageFragmentSpy), any(PermissionListener.class), eq(Manifest.permission.READ_EXTERNAL_STORAGE));
        verify(mImageProviderMock, times(1)).getImageFromGallery(eq(mGetImageFragmentSpy), eq(INTENT_CODE_IMAGE_GALLERY), eq(GALLERY_ERROR_RES_ID));
        verify(callbackMock, times(0)).error(any(GetImageFragment.Error.class));
        verify(callbackMock, times(0)).success(any(File.class));
    }


    @Test
    public void selectImageFromGalleryErrorNotifyCallback() {
        GetImageFragment.OnImageReturnCallback callbackMock = mock(GetImageFragment.OnImageReturnCallback.class);

        doAnswer(invocation -> {
            ((PermissionListener) invocation.getArgument(1)).onPermissionsDenied(new String[1]);
            return null;
        }).when(mPermissionManagerMock).requestPermission(any(Fragment.class), any(PermissionListener.class), eq(Manifest.permission.READ_EXTERNAL_STORAGE));

        mGetImageFragmentSpy.selectImageFromGallery(callbackMock);

        verify(mPermissionManagerMock, times(1)).requestPermission(eq(mGetImageFragmentSpy), any(PermissionListener.class), eq(Manifest.permission.READ_EXTERNAL_STORAGE));
        verify(mImageProviderMock, times(0)).getImageFromGallery(any(Fragment.class), anyInt(), anyInt());
        verify(callbackMock, times(1)).error(eq(GetImageFragment.Error.PERMISSION_DENIED));
        verify(callbackMock, times(0)).success(any(File.class));
    }


   @Test
    public void takePictureSuccessShouldCallDependencies() {
        GetImageFragment.OnImageReturnCallback callbackMock = mock(GetImageFragment.OnImageReturnCallback.class);
        getImageFromCameraSuccess();

        mGetImageFragmentSpy.takePicture(callbackMock);

        verify(mPermissionManagerMock, times(1)).requestPermission(eq(mGetImageFragmentSpy), any(PermissionListener.class), eq(CAMERA_PERMISSIONS[0]), eq(CAMERA_PERMISSIONS[1]));
        verify(mImageProviderMock, times(1)).getImageFromCamera(eq(mGetImageFragmentSpy), eq(INTENT_CODE_IMAGE_CAMERA), eq(CAMERA_FILENAME), eq(ImageProvider.PNG), eq(CAMERA_ERROR_RES_ID));
        verify(callbackMock, times(0)).error(any(GetImageFragment.Error.class));
        verify(callbackMock, times(0)).success(any(File.class));
    }


    @Test
    public void takePictureErrorShouldNotifyCallback() {
        GetImageFragment.OnImageReturnCallback callbackMock = mock(GetImageFragment.OnImageReturnCallback.class);

        doAnswer(invocation -> {
            ((PermissionListener) invocation.getArgument(1)).onPermissionsDenied(new String[1]);
            return null;
        }).when(mPermissionManagerMock).requestPermission(any(Fragment.class), any(PermissionListener.class), eq(CAMERA_PERMISSIONS[0]), eq(CAMERA_PERMISSIONS[1]));

        mGetImageFragmentSpy.takePicture(callbackMock);

        verify(mPermissionManagerMock, times(1)).requestPermission(eq(mGetImageFragmentSpy), any(PermissionListener.class), eq(CAMERA_PERMISSIONS[0]), eq(CAMERA_PERMISSIONS[1]));
        verify(mImageProviderMock, times(0)).getImageFromGallery(any(Fragment.class), anyInt(), anyInt());
        verify(callbackMock, times(1)).error(eq(GetImageFragment.Error.PERMISSION_DENIED));
        verify(callbackMock, times(0)).success(any(File.class));
    }

    @Test
    public void onActivityResultFromGalleryShouldCallCallback() {
        GetImageFragment.OnImageReturnCallback callbackMock = mock(GetImageFragment.OnImageReturnCallback.class);
        getImageFromGallerySuccess();
        mGetImageFragmentSpy.selectImageFromGallery(callbackMock);

        Intent intentMock = mock(Intent.class);
        when(intentMock.getData()).thenReturn(Uri.EMPTY);
        when(mWolmoFileProviderMock.getRealPathFromUri(any(Uri.class))).thenReturn("/");

        // Call Test
        mGetImageFragmentSpy.onActivityResult(INTENT_CODE_IMAGE_GALLERY, Activity.RESULT_OK, intentMock);

        verify(callbackMock, times(1)).success(eq(new File("/")));
    }

    @Test
    public void onActivityResultFromGalleryWithNullDataShouldCallError() {
        GetImageFragment.OnImageReturnCallback callbackMock = mock(GetImageFragment.OnImageReturnCallback.class);
        getImageFromGallerySuccess();
        mGetImageFragmentSpy.selectImageFromGallery(callbackMock);

        Intent intentMock = mock(Intent.class);
        when(intentMock.getData()).thenReturn(null);
        when(mWolmoFileProviderMock.getRealPathFromUri(any(Uri.class))).thenReturn("/");

        // Call Test
        mGetImageFragmentSpy.onActivityResult(INTENT_CODE_IMAGE_GALLERY, Activity.RESULT_OK, intentMock);

        verify(callbackMock, times(1)).error(eq(GetImageFragment.Error.ERROR_DATA));
    }

    @Test
    public void onActivityResultFromCameraShouldCallCallback() {
        GetImageFragment.OnImageReturnCallback callbackMock = mock(GetImageFragment.OnImageReturnCallback.class);
        getImageFromCameraSuccess();
        mGetImageFragmentSpy.takePicture(callbackMock);

        Intent intentMock = mock(Intent.class);
        when(intentMock.getData()).thenReturn(Uri.EMPTY);
        when(mWolmoFileProviderMock.getRealPathFromUri(any(Uri.class))).thenReturn("/");

        // Call Test
        mGetImageFragmentSpy.onActivityResult(INTENT_CODE_IMAGE_CAMERA, Activity.RESULT_OK, intentMock);

        verify(mImageProviderMock, times(1)).addPictureToDeviceGallery(eq(Uri.fromFile(new File("/"))));
        verify(callbackMock, times(1)).success(eq(new File("/")));
    }

    @Test
    public void onActivityResultWithRandomErrorShouldNotifyError() {
        GetImageFragment.OnImageReturnCallback callbackMock = mock(GetImageFragment.OnImageReturnCallback.class);
        getImageFromGallerySuccess();
        mGetImageFragmentSpy.selectImageFromGallery(callbackMock);

        Intent intentMock = mock(Intent.class);

        // Call Test
        mGetImageFragmentSpy.onActivityResult(1234, Activity.RESULT_OK, intentMock);

        verify(callbackMock, times(1)).error(eq(GetImageFragment.Error.ERROR_UNKNOWN));
    }

    @Test
    public void onActivityResultCancelledShouldNotifyCallback() {
        GetImageFragment.OnImageReturnCallback callbackMock = mock(GetImageFragment.OnImageReturnCallback.class);
        getImageFromGallerySuccess();
        mGetImageFragmentSpy.selectImageFromGallery(callbackMock);

        Intent intentMock = mock(Intent.class);

        // Call Test
        mGetImageFragmentSpy.onActivityResult(INTENT_CODE_IMAGE_GALLERY, Activity.RESULT_CANCELED, intentMock);

        verify(callbackMock, times(1)).error(eq(GetImageFragment.Error.USER_CANCELED));
    }

    @Test
    public void onActivityResultCancelledRandomCodeShouldNotifyCallback() {
        GetImageFragment.OnImageReturnCallback callbackMock = mock(GetImageFragment.OnImageReturnCallback.class);
        getImageFromGallerySuccess();
        mGetImageFragmentSpy.selectImageFromGallery(callbackMock);

        Intent intentMock = mock(Intent.class);

        // Call Test
        mGetImageFragmentSpy.onActivityResult(1234, Activity.RESULT_CANCELED, intentMock);

        verify(callbackMock, times(1)).error(eq(GetImageFragment.Error.ERROR_UNKNOWN));
    }

    private void getImageFromGallerySuccess() {
        doAnswer(invocation -> {
            ((PermissionListener) invocation.getArgument(1)).onPermissionsGranted();
            return null;
        }).when(mPermissionManagerMock).requestPermission(any(Fragment.class), any(PermissionListener.class), eq(Manifest.permission.READ_EXTERNAL_STORAGE));
    }

    private void getImageFromCameraSuccess() {
        doAnswer(invocation -> {
            when(mImageProviderMock.getImageFromCamera(any(Fragment.class), anyInt(), anyString(), anyString(), anyInt())).thenReturn(new File("/"));
            ((PermissionListener) invocation.getArgument(1)).onPermissionsGranted();
            return null;
        }).when(mPermissionManagerMock).requestPermission(any(Fragment.class), any(PermissionListener.class), eq(CAMERA_PERMISSIONS[0]), eq(CAMERA_PERMISSIONS[1]));
    }

    public static class TestImageFragment extends GetImageFragment {

        @Override
        public int layout() {
            return 12345;
        }

        @Override
        public void init() {}

        @Override
        protected int galleryErrorResId() {
            return GALLERY_ERROR_RES_ID;
        }

        @Override
        protected int cameraErrorResId() {
            return CAMERA_ERROR_RES_ID;
        }

        @NonNull
        @Override
        protected String pictureTakenFilename() {
            return CAMERA_FILENAME;
        }
    }
}
