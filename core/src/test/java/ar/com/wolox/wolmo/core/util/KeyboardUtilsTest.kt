package ar.com.wolox.wolmo.core.util

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowActivity


@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class KeyboardUtilsTest {

    private var mContextMock: Context? = null

    companion object {
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

    @Before
    fun beforeTest() {
        mContextMock = mock<Context>(Context::class.java)
    }

    @Test
    fun showKeyboardShouldCallInputMethodManager() {
        val managerMock = mock(InputMethodManager::class.java)
        val editTextMock = mock(EditText::class.java)
        `when`(mContextMock?.getSystemService(eq<String>(Context.INPUT_METHOD_SERVICE))).thenReturn(managerMock)

        mContextMock?.let { KeyboardUtils.showKeyboard(it, editTextMock) }

        verify<InputMethodManager>(managerMock, times(1)).showSoftInput(eq<EditText>(editTextMock), eq(InputMethodManager.SHOW_IMPLICIT))
    }

    @Test
    fun hideKeyboardShouldCallInputMethodManager() {
        val managerMock = mock(InputMethodManager::class.java)
        val editTextMock = mock(EditText::class.java)
        val iBinderMock = mock(IBinder::class.java)

        `when`(mContextMock?.getSystemService(eq(Context.INPUT_METHOD_SERVICE))).thenReturn(managerMock)
        `when`(editTextMock.windowToken).thenReturn(iBinderMock)

        mContextMock?.let { KeyboardUtils.hideKeyboard(it, editTextMock) }

        verify(managerMock, times(1)).hideSoftInputFromWindow(eq(iBinderMock), eq(0))
    }

    @Test
    @Config(shadows = [WolmoShadowActivity::class])
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun hideKeyboardWithoutViewShouldCreateView() {
        val activityController = Robolectric.buildActivity<TestKeyboardActivity>(TestKeyboardActivity::class.java).create().start()
        val shadowActivity = Shadow.extract<WolmoShadowActivity>(activityController.get())

        KeyboardUtils.hideKeyboard(activityController.get() as Activity)

        verify<InputMethodManager>(shadowActivity.mInputMethodManagerMock, times(1)).hideSoftInputFromWindow(isNull(), eq(0))
    }
}