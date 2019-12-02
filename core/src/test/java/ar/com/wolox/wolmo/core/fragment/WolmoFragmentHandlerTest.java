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

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import ar.com.wolox.wolmo.core.R;
import ar.com.wolox.wolmo.core.fragment.IWolmoFragment;
import ar.com.wolox.wolmo.core.fragment.WolmoFragment;
import ar.com.wolox.wolmo.core.fragment.WolmoFragmentHandler;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;
import ar.com.wolox.wolmo.core.util.Logger;
import ar.com.wolox.wolmo.core.util.ToastFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WolmoFragmentHandlerTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private WolmoFragmentHandler<TestView, TestPresenter> mWolmoFragmentHandler;
    private WolmoFragment mWolmoFragmentMock;
    private ToastFactory mToastFactoryMock;
    private Logger mLoggerMock;

    @Before
    public void beforeTest() {
        mLoggerMock = mock(Logger.class);
        mToastFactoryMock = mock(ToastFactory.class);
        mWolmoFragmentMock = mock(WolmoFragment.class);
        mWolmoFragmentHandler = new WolmoFragmentHandler<>(mToastFactoryMock, mLoggerMock);
    }

    @Test
    public void setWolmoFragmentNotExtendingFragment() {
        IWolmoFragment wolmoFragment = mock(IWolmoFragment.class);
        exception.expect(IllegalArgumentException.class);
        mWolmoFragmentHandler.onCreate(wolmoFragment);
    }

    @Test
    public void handlerFinishActivityIfWrongArguments() {
        FragmentActivity activity = mock(FragmentActivity.class);

        when(mWolmoFragmentMock.handleArguments(nullable(Bundle.class))).thenReturn(false);
        when(mWolmoFragmentMock.requireActivity()).thenReturn(activity);

        mWolmoFragmentHandler.onCreate(mWolmoFragmentMock);

        verify(mLoggerMock, times(1)).setTag(eq(WolmoFragmentHandler.class.getSimpleName()));
        verify(activity, times(1)).finish();
        verify(mToastFactoryMock, times(1)).show(R.string.unknown_error);
        Throwable nullThrowable = null;
        verify(mLoggerMock, times(1)).e(eq(mWolmoFragmentMock.getClass().getSimpleName() +
                " - The fragment's handleArguments() returned false."), eq(nullThrowable));
    }

    @Test
    public void onViewCreatedShouldCallAttachView() {
        TestFragment testFragment = new TestFragment();
        TestPresenter testPresenterSpy = spy(new TestPresenter());
        mWolmoFragmentHandler = new WolmoFragmentHandler<>(mToastFactoryMock, mLoggerMock, testPresenterSpy);

        when(mWolmoFragmentMock.handleArguments(isNull())).thenReturn(true);
        mWolmoFragmentHandler.onCreate(testFragment);

        mWolmoFragmentHandler.onViewCreated();
        verify(testPresenterSpy, times(1)).attachView(eq(testFragment));
    }

    @Test
    public void onViewCreatedCallsWolmoFragmentMethods() {
        View mockView = mock(View.class);
        when(mWolmoFragmentMock.handleArguments(nullable(Bundle.class))).thenReturn(true);

        mWolmoFragmentHandler.onCreate(mWolmoFragmentMock);
        mWolmoFragmentHandler.onViewCreated();

        // Verify that the methods in wolmoFragment are called in order
        InOrder inOrder = inOrder(mWolmoFragmentMock);

        inOrder.verify(mWolmoFragmentMock, times(1)).setUi(mockView);
        inOrder.verify(mWolmoFragmentMock, times(1)).init();
        inOrder.verify(mWolmoFragmentMock, times(1)).populate();
        inOrder.verify(mWolmoFragmentMock, times(1)).setListeners();
    }

    @Test
    public void notifyFragmentOnVisibilityChanged() {
        mWolmoFragmentHandler.setMenuVisibility(true);

        when(mWolmoFragmentMock.isResumed()).thenReturn(true);
        when(mWolmoFragmentMock.handleArguments(nullable(Bundle.class))).thenReturn(true);

        mWolmoFragmentHandler.onCreate(mWolmoFragmentMock);
        mWolmoFragmentHandler.onViewCreated();
        mWolmoFragmentHandler.onResume();
        verify(mWolmoFragmentMock, times(1)).onVisible();
        verify(mWolmoFragmentMock, times(0)).onHide();

        // Hide the fragment
        when(mWolmoFragmentMock.isResumed()).thenReturn(false);
        mWolmoFragmentHandler.onPause();
        verify(mWolmoFragmentMock, times(1)).onHide();
    }

    @Test
    public void detachesPresenterOnDestroyView() {
        TestPresenter presenter = mock(TestPresenter.class);
        mWolmoFragmentHandler = new WolmoFragmentHandler<>(mToastFactoryMock, mLoggerMock, presenter);

        mWolmoFragmentHandler.onDestroyView();
        verify(presenter, times(1)).detachView();
    }

    @Test
    public void getPresenterWithoutPresenterShouldReturnNull() {
        assertThat(mWolmoFragmentHandler.getPresenter()).isNull();
    }

    @Test
    public void getPresenterShouldReturnThePresenterInstance() {
        TestPresenter presenter = mock(TestPresenter.class);
        mWolmoFragmentHandler = new WolmoFragmentHandler<>(mToastFactoryMock, mLoggerMock, presenter);

        assertThat(presenter).isSameAs(mWolmoFragmentHandler.getPresenter());
    }

    @Test
    public void requirePresenterShouldReturnThePresenterInstance() {
        TestPresenter presenter = mock(TestPresenter.class);
        mWolmoFragmentHandler = new WolmoFragmentHandler<>(mToastFactoryMock, mLoggerMock, presenter);

        assertThat(presenter).isSameAs(mWolmoFragmentHandler.requirePresenter());
        assertThat(mWolmoFragmentHandler.getPresenter()).isSameAs(mWolmoFragmentHandler.requirePresenter());
    }

    @Test
    public void requirePresenterShouldThrowExceptionIfPresenterIsNull() {
        exception.expect(NullPointerException.class);
        mWolmoFragmentHandler.requirePresenter();
    }

    interface TestView {}

    static class TestFragment extends WolmoFragment<TestView, TestPresenter> implements TestView {
        @Override
        public int layout() {
            return 0;
        }

        @Override
        public void init() {}
    }

    static class TestPresenter extends BasePresenter<TestView> {}

}
