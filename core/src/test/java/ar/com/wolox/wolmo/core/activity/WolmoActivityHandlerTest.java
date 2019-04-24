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

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import ar.com.wolox.wolmo.core.R;
import ar.com.wolox.wolmo.core.util.ToastFactory;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = Build.VERSION_CODES.LOLLIPOP)
public class WolmoActivityHandlerTest {

    private ToastFactory mToastFactoryMock;
    private WolmoActivity mWolmoActivitySpy;
    private WolmoActivityHandler mWolmoActivityHandlerSpy;

    @Before
    public void beforeTest() {
        mWolmoActivitySpy = spy(Robolectric.buildActivity(TestActivity.class).get());
        mToastFactoryMock = mock(ToastFactory.class);

        mWolmoActivityHandlerSpy = spy(new WolmoActivityHandler(mToastFactoryMock));
    }

    @Test
    public void onCreateShouldCallWolmoMethods() {
        mWolmoActivityHandlerSpy.onCreate(mWolmoActivitySpy, null);

        // Verify that the methods in wolmoFragment are called in order
        InOrder inOrder = Mockito.inOrder(mWolmoActivitySpy);

        inOrder.verify(mWolmoActivitySpy, times(1)).handleArguments(null);
        inOrder.verify(mWolmoActivitySpy, times(1)).setUi();
        inOrder.verify(mWolmoActivitySpy, times(1)).init();
        inOrder.verify(mWolmoActivitySpy, times(1)).populate();
        inOrder.verify(mWolmoActivitySpy, times(1)).setListeners();
    }

    @Test
    public void onCreateWithFailHandlingArgsShouldFinishActivity() {
        when(mWolmoActivitySpy.handleArguments(isNull())).thenReturn(false);
        mWolmoActivityHandlerSpy.onCreate(mWolmoActivitySpy, null);

        verify(mToastFactoryMock, times(1)).show(eq(R.string.unknown_error));
        verify(mWolmoActivitySpy, times(1)).finish();
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
