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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.io.File;
import java.io.IOException;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = Build.VERSION_CODES.LOLLIPOP, shadows = WolmoFileProviderTest.ShadowFileProvider.class)
public class WolmoFileProviderTest {

    private Context mContextSpy;
    private WolmoFileProvider mWolmoFileProvider;

    @Before
    public void beforeTest() {
        mContextSpy = spy(RuntimeEnvironment.application);
        mWolmoFileProvider= new WolmoFileProvider(mContextSpy);
    }

    @Test
    public void createTempFileShouldCreateNewFiles() throws IOException {
        mWolmoFileProvider.createTempFile("TestFile", "txt", Environment.DIRECTORY_DCIM);
        mWolmoFileProvider.createTempFile("WithDot", ".txt", Environment.DIRECTORY_DCIM);

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        assertThat(storageDir.listFiles()).anyMatch(file -> file.getName().matches("TestFile.*\\.txt"));
        assertThat(storageDir.listFiles()).anyMatch(file -> file.getName().matches("WithDot.*\\.txt"));
    }

    @Test
    public void getUriForFileShouldReturnExistingUri() throws IOException {
        File file = File.createTempFile("getUri", ".txt", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));

        // Check that we are calling FileProvider
        assertThat(mWolmoFileProvider.getUriForFile(file)).isEqualTo(Uri.EMPTY);
        assertThat(ShadowFileProvider.sContext).isSameAs(mContextSpy);
        assertThat(ShadowFileProvider.sAuthority).isEqualTo("org.robolectric.default.provider");
        assertThat(ShadowFileProvider.sFile).isSameAs(file);
    }

    @Test
    public void getRealPathFromUriShouldReturnExistingPath() throws IOException {
        assertThat(mWolmoFileProvider.getRealPathFromUri(Uri.EMPTY)).isNull();
    }

    @Implements(FileProvider.class)
    public static class ShadowFileProvider {
        static Context sContext;
        static String sAuthority;
        static File sFile;

        @Implementation
        public static Uri getUriForFile(Context context, String authority, File file) {
            sContext = context;
            sAuthority = authority;
            sFile = file;
            return Uri.EMPTY;
        }
    }
}

