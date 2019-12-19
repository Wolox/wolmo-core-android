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
package ar.com.wolox.wolmo.core.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.ArgumentMatchers.isNull
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowActivity

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [Build.VERSION_CODES.O_MR1])
class KeyboardManagerTest {

    private lateinit var context: Context
    private lateinit var keyboardManager: KeyboardManager

    @Before
    fun setup() {
        context = mock(Context::class.java)
        keyboardManager = KeyboardManager(context)
    }

    @Test
    fun `given a keyboard with a mock when show it then invoke show soft input`() {
        val inputMethodManager = mock(InputMethodManager::class.java)
        val editText = mock(EditText::class.java)
        `when`(context.getSystemService(eq(Context.INPUT_METHOD_SERVICE))).thenReturn(inputMethodManager)

        keyboardManager.show(editText)

        verify(inputMethodManager, times(1)).showSoftInput(eq(editText), eq(InputMethodManager.SHOW_IMPLICIT))
    }

    @Test
    fun `given a keyboard with a mock when hide it then invoke hide soft input`() {
        val inputMethodManager = mock(InputMethodManager::class.java)
        val editText = mock(EditText::class.java)
        val binder = mock(IBinder::class.java)

        `when`(context.getSystemService(eq(Context.INPUT_METHOD_SERVICE))).thenReturn(inputMethodManager)
        `when`(editText.windowToken).thenReturn(binder)

        keyboardManager.hide(editText)

        verify(inputMethodManager, times(1)).hideSoftInputFromWindow(eq(binder), eq(0))
    }

    @Test
    @Config(shadows = [WolmoShadowActivity::class])
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun `given a keyboard with when hide it on activity without view then create a view and invoke hide soft input`() {
        val activityController = Robolectric.buildActivity(TestKeyboardActivity::class.java).create().start()
        val shadowActivity = Shadow.extract<WolmoShadowActivity>(activityController.get())

        keyboardManager.hide(activityController.get() as Activity)

        verify(shadowActivity.mInputMethodManagerMock, times(1)).hideSoftInputFromWindow(isNull(), eq(0))
    }

    /**
     * Activity to test keyboard utils
     */
    class TestKeyboardActivity : Activity()

    /**
     * Returns a mock of inputManager to track calls
     */
    @Implements(Activity::class)
    class WolmoShadowActivity : ShadowActivity() {
        internal lateinit var mInputMethodManagerMock: InputMethodManager

        @Implementation
        fun getSystemService(name: String): Any {
            if (name == Activity.INPUT_METHOD_SERVICE) {
                mInputMethodManagerMock = mock(InputMethodManager::class.java)
                return mInputMethodManagerMock
            }
            return Shadow.directlyOn(realActivity, Activity::class.java).getSystemService(name)
        }
    }
}
