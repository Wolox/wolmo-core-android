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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import ar.com.wolox.wolmo.core.util.ToastFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import butterknife.ButterKnife;
import butterknife.Unbinder;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ButterKnife.class})
public class WolmoActivityHandlerTest {

    private ToastFactory mToastFactory;
    private WolmoActivity mWolmoActivity;
    private WolmoActivityHandler mWolmoActivityHandler;

    @Before
    public void beforeTest() {
        mWolmoActivity = mock(WolmoActivity.class);
        mToastFactory = mock(ToastFactory.class);

        mWolmoActivityHandler = new WolmoActivityHandler(mToastFactory);

        // Mock calls to android.util.Log
        PowerMockito.mockStatic(ButterKnife.class);
    }

    @Test
    public void onCreateShouldCallWolmoMethods() {
        when(mWolmoActivity.handleArguments(isNull(Bundle.class))).thenReturn(true);
        when(mWolmoActivity.getIntent()).thenReturn(mock(Intent.class));

        mWolmoActivityHandler.onCreate(mWolmoActivity, null);

        // Verify that the methods in wolmoFragment are called in order
        InOrder inOrder = Mockito.inOrder(mWolmoActivity);

        inOrder.verify(mWolmoActivity, times(1)).handleArguments(null);
        inOrder.verify(mWolmoActivity, times(1)).setUi();
        inOrder.verify(mWolmoActivity, times(1)).init();
        inOrder.verify(mWolmoActivity, times(1)).populate();
        inOrder.verify(mWolmoActivity, times(1)).setListeners();
    }

    @Test
    public void onDestroyShouldCallUnbind() {
        Unbinder unbinderMock = mock(Unbinder.class);
        when(ButterKnife.bind(any(Activity.class))).thenReturn(unbinderMock);
        when(mWolmoActivity.handleArguments(isNull(Bundle.class))).thenReturn(true);
        when(mWolmoActivity.getIntent()).thenReturn(mock(Intent.class));

        mWolmoActivityHandler.onCreate(mWolmoActivity, null);
        mWolmoActivityHandler.onDestroy();

        verify(unbinderMock, times(1)).unbind();
    }
}
