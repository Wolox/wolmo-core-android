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
package ar.com.wolox.wolmo.core.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.widget.Toast;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import ar.com.wolox.wolmo.core.R;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ToastFactoryTest {

    private Context mContextSpy;
    private ToastFactory mToastFactory;

    @Before
    public void beforeTest() {
        mContextSpy = spy(ApplicationProvider.getApplicationContext());
        mToastFactory = new ToastFactory(mContextSpy);

        Resources resourcesMock = mock(Resources.class);
        when(resourcesMock.getString(anyInt())).thenReturn("MockString");
        when(mContextSpy.getResources()).thenReturn(resourcesMock);
    }

    @Test
    public void showShouldShowAShortToast() {
        mToastFactory.show("ShowToast");
        assertThat(ShadowToast.getLatestToast().getDuration()).isEqualTo(Toast.LENGTH_SHORT);
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("ShowToast");

        mToastFactory.show(R.string.unknown_error);
        assertThat(ShadowToast.getLatestToast().getDuration()).isEqualTo(Toast.LENGTH_SHORT);
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("MockString");
    }

    @Test
    public void showLongShouldShowALongToast() {
        mToastFactory.showLong("ShowToast");
        assertThat(ShadowToast.getLatestToast().getDuration()).isEqualTo(Toast.LENGTH_LONG);
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("ShowToast");

        mToastFactory.showLong(R.string.unknown_error);
        assertThat(ShadowToast.getLatestToast().getDuration()).isEqualTo(Toast.LENGTH_LONG);
        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("MockString");
    }

}

