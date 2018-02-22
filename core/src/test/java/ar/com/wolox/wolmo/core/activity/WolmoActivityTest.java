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
package ar.com.wolox.wolmo.core.activity;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ar.com.wolox.wolmo.core.fragment.WolmoFragment;
import ar.com.wolox.wolmo.core.permission.PermissionManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk= Build.VERSION_CODES.LOLLIPOP)
public class WolmoActivityTest {

    private WolmoActivity mWolmoActivity;
    private WolmoActivityHandler mWolmoActivityHandlerMock;
    private PermissionManager mPermissionManagerMock;

    @Before
    public void beforeTest() {
        mPermissionManagerMock = mock(PermissionManager.class);
        mWolmoActivityHandlerMock = mock(WolmoActivityHandler.class);

        mWolmoActivity = Robolectric.buildActivity(TestActivity.class).get();
        mWolmoActivity.mActivityHandler = mWolmoActivityHandlerMock;
        mWolmoActivity.mPermissionManager = mPermissionManagerMock;
    }

    @Test
    public void onRequestPermissionResultShouldCallManager() {
        String[] permissions = new String[]{"asd"};
        int[] grantResults = new int[]{321};
        mWolmoActivity.onRequestPermissionsResult(123, permissions, grantResults);

        verify(mPermissionManagerMock, times(1)).onRequestPermissionsResult(eq(123), eq(permissions), eq(grantResults));
    }

    @Test
    public void finishActivityWhenHomeOptionIsSelected() {
        mWolmoActivity = spy(mWolmoActivity);
        MenuItem menuItem = mock(MenuItem.class);
        doReturn(android.R.id.home).when(menuItem).getItemId();
        doNothing().when(mWolmoActivity).finish();

        assertThat(mWolmoActivity.onOptionsItemSelected(menuItem)).isTrue();
        verify(mWolmoActivity, times(1)).finish();
    }

    @Test
    public void callSuperWhenExtraOptionIsSelected() {
        mWolmoActivity = spy(mWolmoActivity);
        MenuItem menuItem = mock(MenuItem.class);
        doReturn(android.R.id.addToDictionary).when(menuItem).getItemId();
        doReturn(false).when((AppCompatActivity) mWolmoActivity).onOptionsItemSelected(any(MenuItem.class));

        assertThat(mWolmoActivity.onOptionsItemSelected(menuItem)).isFalse();
        verify(mWolmoActivity, times(0)).finish();
        verify((AppCompatActivity) mWolmoActivity, times(1)).onOptionsItemSelected(menuItem);
    }

    @Test
    @SuppressWarnings("RestrictedApi")
    public void backPressedNotifiesChildFragments() {
        mWolmoActivity = spy(mWolmoActivity);

        // This fragment does not handle back pressed
        WolmoFragment wolmoFragment = mock(WolmoFragment.class);
        doReturn(true).when(wolmoFragment).isVisible();
        doReturn(false).when(wolmoFragment).onBackPressed();

        // So we continue in the list until this fragment does handle it
        WolmoFragment wolmoFragment2 = mock(WolmoFragment.class);
        doReturn(true).when(wolmoFragment2).isVisible();
        doReturn(true).when(wolmoFragment2).onBackPressed();
        // We need to handle the last one because the activity calls super and we cannot mock it

        List<WolmoFragment> fragments = Arrays.asList(wolmoFragment, wolmoFragment2);

        FragmentManager manager = mock(FragmentManager.class);
        doReturn(fragments).when(manager).getFragments();
        doReturn(manager).when(mWolmoActivity).getSupportFragmentManager();

        // Check that the activity passes the call to the fragments
        mWolmoActivity.onBackPressed();
        verify(wolmoFragment, times(1)).onBackPressed();
        verify(wolmoFragment2, times(1)).onBackPressed();
    }

    @Test
    @SuppressWarnings("RestrictedApi")
    public void backPressedNotifiesChildFragmentsAndFirstOneHandlesIt() {
        mWolmoActivity = spy(mWolmoActivity);

        // This fragment does not handle back pressed
        WolmoFragment wolmoFragment = mock(WolmoFragment.class);
        doReturn(true).when(wolmoFragment).isVisible();
        doReturn(true).when(wolmoFragment).onBackPressed();

        // So we continue in the list until this fragment does handle it
        WolmoFragment wolmoFragment2 = mock(WolmoFragment.class);
        doReturn(true).when(wolmoFragment2).isVisible();
        doReturn(true).when(wolmoFragment2).onBackPressed();
        // We need to handle the last one because the activity calls super and we cannot mock it

        List<WolmoFragment> fragments = Arrays.asList(wolmoFragment, wolmoFragment2);

        FragmentManager manager = mock(FragmentManager.class);
        doReturn(fragments).when(manager).getFragments();
        doReturn(manager).when(mWolmoActivity).getSupportFragmentManager();

        // Check that the activity passes the call to the fragments
        mWolmoActivity.onBackPressed();
        verify(wolmoFragment, times(1)).onBackPressed();
        verify(wolmoFragment2, times(0)).onBackPressed();
    }


    public static class TestActivity extends WolmoActivity {

        @Override
        protected int layout() {
            return 0;
        }

        @Override
        protected void init() {}

        @Override
        public void setContentView(int layoutResID) {}
    }
}
