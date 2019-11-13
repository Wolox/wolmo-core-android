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
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * An utility class to work with Android's keyboard (that means the software keyboard, not a physical one).
 */
@Deprecated("Replace it by injecting [KeyboardManager].")
object KeyboardUtils {

    private fun getInputMethodManager(context: Context) =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    /** Forces the soft keyboard to show in a [context] for a specific [editText]. */
    @JvmStatic
    fun showKeyboard(context: Context, editText: EditText) {
        getInputMethodManager(context).showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    /** Forces the soft keyboard to hide in a [context], meant to be called from inside a Fragment's [view]. */
    @JvmStatic
    fun hideKeyboard(context: Context, view: View) {
        getInputMethodManager(context).hideSoftInputFromWindow(view.windowToken, 0)
    }

    /** Forces the device's soft keyboard to hide, meant to be called from inside an [activity]. */
    @JvmStatic
    fun hideKeyboard(activity: Activity) {
        // If no view currently has focus, create a new one,
        // just so we can grab a window token from it
        val view = activity.currentFocus ?: View(activity)
        getInputMethodManager(activity).hideSoftInputFromWindow(view.windowToken, 0)
    }
}