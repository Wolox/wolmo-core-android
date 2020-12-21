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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Test;

import ar.com.wolox.wolmo.core.permission.PermissionManager;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WolmoBottomSheetDialogFragmentTest {

    private WolmoFragmentHandler<?, BasePresenter<?>> mWolmoFragmentHandlerMock;
    private PermissionManager mPermissionManagerMock;
    private WolmoBottomSheetDialogFragment mWolmoBottomSheetDialogFragmentSpy;

    @Before
    @SuppressWarnings("unchecked")
    public void beforeTest() {
        mWolmoFragmentHandlerMock = mock(WolmoFragmentHandler.class);
        mPermissionManagerMock = mock(PermissionManager.class);

        mWolmoBottomSheetDialogFragmentSpy = spy(WolmoBottomSheetDialogFragment.class);
        mWolmoBottomSheetDialogFragmentSpy.fragmentHandler = mWolmoFragmentHandlerMock;
        mWolmoBottomSheetDialogFragmentSpy.permissionManager = mPermissionManagerMock;
    }

    @Test
    public void onCreateViewShouldDelegateCall() {
        LayoutInflater inflaterMock = mock(LayoutInflater.class);
        ViewGroup viewGroupMock = mock(ViewGroup.class);

        mWolmoBottomSheetDialogFragmentSpy.onCreateView(inflaterMock, viewGroupMock, null);
        verify(mWolmoFragmentHandlerMock, times(1)).onCreateView(eq(inflaterMock), eq(viewGroupMock));
    }

    @Test
    public void onViewCreatedShouldDelegateCall() {
        View viewMock = mock(View.class);

        mWolmoBottomSheetDialogFragmentSpy.onViewCreated(viewMock, null);
        verify(mWolmoFragmentHandlerMock, times(1)).onViewCreated(viewMock);
    }

    @Test
    public void setMenuVisibilityShouldDelegateCall() {
        mWolmoBottomSheetDialogFragmentSpy.setMenuVisibility(true);
        verify(mWolmoFragmentHandlerMock, times(1)).setMenuVisibility(eq(true));
    }

    @Test
    public void onResumeShouldDelegateCall() {
        mWolmoBottomSheetDialogFragmentSpy.onResume();
        verify(mWolmoFragmentHandlerMock, times(1)).onResume();
    }

    @Test
    public void onPauseShouldDelegateCall() {
        mWolmoBottomSheetDialogFragmentSpy.onPause();
        verify(mWolmoFragmentHandlerMock, times(1)).onPause();
    }

    @Test
    public void onDestroyViewShouldDelegateCall() {
        mWolmoBottomSheetDialogFragmentSpy.onDestroyView();
        verify(mWolmoFragmentHandlerMock, times(1)).onDestroyView();
    }

    @Test
    public void onRequestPermissionResultShouldDelegateCall() {
        String[] permissions = new String[] { "perms" };
        int[] grantResults = new int[] { 123456 };

        mWolmoBottomSheetDialogFragmentSpy.onRequestPermissionsResult(123, permissions, grantResults);
        verify(mPermissionManagerMock, times(1)).onRequestPermissionsResult(eq(123), eq(permissions), eq(grantResults));
    }

    @Test
    public void requirePresenterShouldDelegateCall() {
        when(mWolmoFragmentHandlerMock.getPresenter()).thenReturn(new BasePresenter<>());

        mWolmoBottomSheetDialogFragmentSpy.getPresenter();
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
