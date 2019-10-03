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
package ar.com.wolox.wolmo.core.presenter;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import androidx.core.util.Consumer;

public class BasePresenterTest {

    private BasePresenter<Object> mBasePresenter;

    @Before
    @SuppressWarnings("unchecked")
    public void beforeTest() {
        mBasePresenter = spy(BasePresenter.class);
    }


    @Test
    public void viewAttachedUpdates() {
        assertThat(mBasePresenter.isViewAttached()).isEqualTo(false);

        mBasePresenter.attachView(new Object());
        assertThat(mBasePresenter.isViewAttached()).isEqualTo(true);

        mBasePresenter.detachView();
        assertThat(mBasePresenter.isViewAttached()).isEqualTo(false);
    }

    @Test
    public void presenterNotifiesChildClasses() {
        // Attach and detach the view
        mBasePresenter.attachView(new Object());
        verify(mBasePresenter, times(1)).onViewAttached();
        verify(mBasePresenter, times(0)).onViewDetached();

        mBasePresenter.detachView();
        verify(mBasePresenter, times(1)).onViewDetached();
    }

    @Test
    public void presenterRunIfViewAttachedRunnable() {
        Runnable runnableMock = mock(Runnable.class);

        mBasePresenter.runIfViewAttached(runnableMock);
        verify(runnableMock, times(0)).run();

        // Now the view is attached
        mBasePresenter.attachView(new Object());
        mBasePresenter.runIfViewAttached(runnableMock);
        verify(runnableMock, times(1)).run();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void presenterRunIfViewAttachedConsumer() {
        Consumer<Object> consumerMock = mock(Consumer.class);

        mBasePresenter.runIfViewAttached(consumerMock);
        verify(consumerMock, times(0)).accept(any(Object.class));

        // Now the view is attached
        mBasePresenter.attachView(new Object());
        mBasePresenter.runIfViewAttached(consumerMock);
        verify(consumerMock, times(1)).accept(any(Object.class));
    }

}
