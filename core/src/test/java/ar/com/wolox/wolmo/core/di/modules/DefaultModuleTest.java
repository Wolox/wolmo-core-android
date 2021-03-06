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
package ar.com.wolox.wolmo.core.di.modules;

import android.util.SparseArray;

import org.junit.Test;
import org.mockito.Mockito;

import ar.com.wolox.wolmo.core.fragment.WolmoFragmentHandler;
import ar.com.wolox.wolmo.core.permission.PermissionListener;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;
import ar.com.wolox.wolmo.core.util.Logger;
import ar.com.wolox.wolmo.core.util.ToastFactory;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class DefaultModuleTest {

    @Test
    public void providePermissionManagerShouldReturnNewArray() {
        DefaultModule defaultModule = new DefaultModule();
        SparseArray<PermissionListener> array1 = defaultModule.providesPermissionManagerArray();
        SparseArray<PermissionListener> array2 = defaultModule.providesPermissionManagerArray();

        assertThat(array1).isNotNull().isNotSameAs(array2);
    }

    @Test
    public void provideDefaultBasePresenterShouldReturnNewInstance() {
        DefaultModule defaultModule = new DefaultModule();
        BasePresenter basePresenter = defaultModule.providesDefaultBasePresenter();
        BasePresenter basePresenter2 = defaultModule.providesDefaultBasePresenter();

        assertThat(basePresenter).isNotNull().isNotSameAs(basePresenter2);
    }

    @Test
    public void provideDefaultWolmoFragmentHandlerShouldReturnNewInstance() {
        ToastFactory toastFactoryMock = Mockito.mock(ToastFactory.class);
        Logger loggerMock = Mockito.mock(Logger.class);
        BasePresenter basePresenter = Mockito.mock(BasePresenter.class);
        DefaultModule defaultModule = new DefaultModule();

        WolmoFragmentHandler wolmoFragmentHandler =
                defaultModule.providesDefaultWolmoFragmentHandler(toastFactoryMock, loggerMock, basePresenter);
        WolmoFragmentHandler wolmoFragmentHandler2 =
                defaultModule.providesDefaultWolmoFragmentHandler(toastFactoryMock, loggerMock, basePresenter);

        assertThat(wolmoFragmentHandler).isNotNull().isNotSameAs(wolmoFragmentHandler2);
        assertThat(wolmoFragmentHandler).extracting("mToastFactory").containsExactly(toastFactoryMock);
        assertThat(wolmoFragmentHandler).extracting("mLogger").containsExactly(loggerMock);
        assertThat(wolmoFragmentHandler.getPresenter()).isSameAs(basePresenter);
    }
}
