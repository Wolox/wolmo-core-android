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

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowActivity;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = Build.VERSION_CODES.O_MR1)
public class KeyboardUtilsTest {

    private Context mContextMock;

    @Before
    @SuppressWarnings("unchecked")
    public void beforeTest() {
        mContextMock = mock(Context.class);
    }

    @Test
    public void showKeyboardShouldCallInputMethodManager() {
        InputMethodManager managerMock = mock(InputMethodManager.class);
        EditText editTextMock = mock(EditText.class);
        when(mContextMock.getSystemService(eq(Context.INPUT_METHOD_SERVICE))).thenReturn(managerMock);

        KeyboardUtils.showKeyboard(mContextMock, editTextMock);

        verify(managerMock, times(1)).showSoftInput(eq(editTextMock), eq(InputMethodManager.SHOW_IMPLICIT));
    }

    @Test
    public void hideKeyboardShouldCallInputMethodManager() {
        InputMethodManager managerMock = mock(InputMethodManager.class);
        EditText editTextMock = mock(EditText.class);
        IBinder iBinderMock = mock(IBinder.class);

        when(mContextMock.getSystemService(eq(Context.INPUT_METHOD_SERVICE))).thenReturn(managerMock);
        when(editTextMock.getWindowToken()).thenReturn(iBinderMock);

        KeyboardUtils.hideKeyboard(mContextMock, editTextMock);

        verify(managerMock, times(1)).hideSoftInputFromWindow(eq(iBinderMock), eq(0));
    }

    @Test
    @Config(shadows = WolmoShadowActivity.class)
    public void hideKeyboardWithoutViewShouldCreateView() throws NoSuchFieldException, IllegalAccessException {
        ActivityController activityController = Robolectric.buildActivity(TestKeyboardActivity.class).create().start();
        WolmoShadowActivity shadowActivity = Shadow.extract(activityController.get());

        KeyboardUtils.hideKeyboard((Activity) activityController.get());

        verify(shadowActivity.mInputMethodManagerMock, times(1)).hideSoftInputFromWindow(isNull(), eq(0));
    }

    /**
     * Activity to test keyboard utils
     */
    public static class TestKeyboardActivity extends Activity {}

    /**
     * Returns a mock of inputManager to track calls
     */
    @Implements(Activity.class)
    public static class WolmoShadowActivity extends ShadowActivity {
        InputMethodManager mInputMethodManagerMock;

        @Implementation
        public Object getSystemService(String name) {
            if (name.equals(Activity.INPUT_METHOD_SERVICE)) {
                mInputMethodManagerMock = mock(InputMethodManager.class);
                return mInputMethodManagerMock;
            }
            return Shadow.directlyOn(realActivity, Activity.class).getSystemService(name);
        }
    }
}
