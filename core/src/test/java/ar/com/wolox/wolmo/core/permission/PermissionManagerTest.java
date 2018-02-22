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
package ar.com.wolox.wolmo.core.permission;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = Build.VERSION_CODES.LOLLIPOP)
public class PermissionManagerTest {

    private Context mContext;
    private PermissionListener mPermissionListener;
    private SparseArray<PermissionListener> mListenerSparseArray;
    private PermissionManager mPermissionManager;

    @Before
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public void beforeTest() {
        mContext = mock(Context.class);
        mPermissionListener = mock(PermissionListener.class);
        mListenerSparseArray = mock(SparseArray.class);
        mPermissionManager = new PermissionManager(mContext, mListenerSparseArray);

        // When checking permissions in the context, this will return PERMISSION_GRANTED if the
        // permission is named "GRANTED" and return PERMISSION_DENIED otherwise
        when(mContext.checkPermission(not(eq("GRANTED")), anyInt(), anyInt())).thenReturn(
            PackageManager.PERMISSION_DENIED);
        when(mContext.checkPermission(eq("GRANTED"), anyInt(), anyInt())).thenReturn(
            PackageManager.PERMISSION_GRANTED);
    }

    @Test
    public void requestFragmentPermissionAlreadyGranted() {
        Fragment fragment = mock(Fragment.class);

        assertThat(mPermissionManager.requestPermission(fragment, mPermissionListener, "GRANTED"))
            .isTrue();

        verify(mPermissionListener, times(1)).onPermissionsGranted();
        verify(mPermissionListener, times(0)).onPermissionsDenied(any(String[].class));
    }

    @Test
    public void requestActivityPermissionAlreadyGranted() {
        Activity activity = mock(Activity.class);

        assertThat(mPermissionManager.requestPermission(activity, mPermissionListener, "GRANTED"))
            .isTrue();

        verify(mPermissionListener, times(1)).onPermissionsGranted();
        verify(mPermissionListener, times(0)).onPermissionsDenied(any(String[].class));
    }

    @Test
    public void requestFragmentPermission() {
        Fragment fragment = mock(Fragment.class);

        assertThat(mPermissionManager.requestPermission(fragment, mPermissionListener, "DENIED"))
            .isFalse();

        verify(mPermissionListener, times(0)).onPermissionsGranted();
        verify(mPermissionListener, times(0)).onPermissionsDenied(any(String[].class));
        verify(mListenerSparseArray, times(1)).put(anyInt(), eq(mPermissionListener));
        verify(fragment, times(1)).requestPermissions(any(String[].class), anyInt());
    }

    @Test
    public void requestActivityPermission() {
        Activity activity = mock(Activity.class);

        assertThat(mPermissionManager.requestPermission(activity, mPermissionListener, "DENIED"))
            .isFalse();

        verify(mPermissionListener, times(0)).onPermissionsGranted();
        verify(mPermissionListener, times(0)).onPermissionsDenied(any(String[].class));
        verify(mListenerSparseArray, times(1)).put(anyInt(), eq(mPermissionListener));
    }

    @Test
    public void onPermissionGranted() throws Exception {
        when(mListenerSparseArray.get(anyInt())).thenReturn(mPermissionListener);

        mPermissionManager.onRequestPermissionsResult(1, new String[] { "GRANTED" },
            new int[] { PackageManager.PERMISSION_GRANTED });

        verify(mListenerSparseArray, times(1)).remove(anyInt());
        verify(mPermissionListener, times(1)).onPermissionsGranted();
        verify(mPermissionListener, times(0)).onPermissionsDenied(any(String[].class));
    }

    @Test
    public void onPermissionDenied() throws Exception {
        when(mListenerSparseArray.get(anyInt())).thenReturn(mPermissionListener);

        mPermissionManager.onRequestPermissionsResult(1, new String[] { "DENIED", "GRANTED" },
            new int[] { PackageManager.PERMISSION_DENIED, PackageManager.PERMISSION_GRANTED });

        verify(mListenerSparseArray, times(1)).remove(anyInt());
        verify(mPermissionListener, times(0)).onPermissionsGranted();
        verify(mPermissionListener, times(1)).onPermissionsDenied(eq(new String[] { "DENIED" }));
    }
}
