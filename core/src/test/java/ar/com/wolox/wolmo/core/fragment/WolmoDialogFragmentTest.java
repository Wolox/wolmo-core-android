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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

import android.app.Dialog;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import ar.com.wolox.wolmo.core.permission.PermissionManager;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = Build.VERSION_CODES.LOLLIPOP)
public class WolmoDialogFragmentTest {

    private WolmoFragmentHandler<BasePresenter> mWolmoFragmentHandlerMock;
    private PermissionManager mPermissionManagerMock;
    private WolmoDialogFragment mWolmoDialogFragmentSpy;

    @Before
    @SuppressWarnings("unchecked")
    public void beforeTest() {
        mWolmoFragmentHandlerMock = mock(WolmoFragmentHandler.class);
        mPermissionManagerMock = mock(PermissionManager.class);

        mWolmoDialogFragmentSpy = spy(new TestDialog());
        mWolmoDialogFragmentSpy.mFragmentHandler = mWolmoFragmentHandlerMock;
        mWolmoDialogFragmentSpy.mPermissionManager = mPermissionManagerMock;
    }

    @Test
    public void onCreateDialogShouldConfigureSuper() {
        startFragment(mWolmoDialogFragmentSpy);
        Dialog dialog = mWolmoDialogFragmentSpy.getDialog();

        assertThat(dialog.getWindow()).isNotNull();
        assertThat(dialog.getWindow()).extracting("mFeatures").anyMatch(
                feature -> ((int)feature & Window.FEATURE_NO_TITLE) == Window.FEATURE_NO_TITLE);
        assertThat(dialog.getWindow().getAttributes().flags).matches(
                flag -> (flag & WindowManager.LayoutParams.FLAG_DIM_BEHIND) == 0);
    }

    @Test
    public void onStartShouldConfigureDialog() {
        startFragment(mWolmoDialogFragmentSpy);
        Dialog dialog = mWolmoDialogFragmentSpy.getDialog();

        assertThat(dialog.getWindow()).isNotNull();
        assertThat(dialog.getWindow().getAttributes().width).isEqualTo(ViewGroup.LayoutParams.MATCH_PARENT);
        assertThat(dialog.getWindow().getAttributes().height).isEqualTo(ViewGroup.LayoutParams.MATCH_PARENT);

        verify(mWolmoDialogFragmentSpy, times(1)).getBackgroundDrawable();
        assertThat(dialog.getWindow().getDecorView().getBackground()).isEqualTo(mWolmoDialogFragmentSpy.getBackgroundDrawable());
    }

    @Test
    public void onCreateShouldDelegateCall() {
        mWolmoDialogFragmentSpy.onCreate(null);
        verify(mWolmoFragmentHandlerMock, times(1)).onCreate(eq(mWolmoDialogFragmentSpy), isNull());
    }

    @Test
    public void onCreateViewShouldDelegateCall() {
        LayoutInflater inflaterMock = mock(LayoutInflater.class);
        ViewGroup viewGroupMock = mock(ViewGroup.class);

        mWolmoDialogFragmentSpy.onCreateView(inflaterMock, viewGroupMock, null);
        verify(mWolmoFragmentHandlerMock, times(1)).onCreateView(eq(inflaterMock), eq(viewGroupMock), isNull());
    }

    @Test
    public void onViewCreatedShouldDelegateCall() {
        View viewMock = mock(View.class);

        mWolmoDialogFragmentSpy.onViewCreated(viewMock, null);
        verify(mWolmoFragmentHandlerMock, times(1)).onViewCreated(eq(viewMock), isNull());
    }

    @Test
    public void setMenuVisibilityShouldDelegateCall() {
        mWolmoDialogFragmentSpy.setMenuVisibility(true);
        verify(mWolmoFragmentHandlerMock, times(1)).setMenuVisibility(eq(true));
    }

    @Test
    public void onResumeShouldDelegateCall() {
        mWolmoDialogFragmentSpy.onResume();
        verify(mWolmoFragmentHandlerMock, times(1)).onResume();
    }

    @Test
    public void onPauseShouldDelegateCall() {
        mWolmoDialogFragmentSpy.onPause();
        verify(mWolmoFragmentHandlerMock, times(1)).onPause();
    }

    @Test
    public void onDestroyViewShouldDelegateCall() {
        mWolmoDialogFragmentSpy.onDestroyView();
        verify(mWolmoFragmentHandlerMock, times(1)).onDestroyView();
    }

    @Test
    public void onRequestPermissionResultShouldDelegateCall() {
        String[] permissions = new String[] { "perms" };
        int[] grantResults = new int[] { 123456 };

        mWolmoDialogFragmentSpy.onRequestPermissionsResult(123, permissions, grantResults);
        verify(mPermissionManagerMock, times(1)).onRequestPermissionsResult(eq(123), eq(permissions), eq(grantResults));
    }

    @Test
    public void getPresenterShouldDelegateCall() {
        mWolmoDialogFragmentSpy.getPresenter();
        verify(mWolmoFragmentHandlerMock, times(1)).getPresenter();
    }

    public static class TestDialog extends WolmoDialogFragment {

        @Override
        public int layout() {
            return 12345;
        }

        @Override
        public void init() {}
    }
}
