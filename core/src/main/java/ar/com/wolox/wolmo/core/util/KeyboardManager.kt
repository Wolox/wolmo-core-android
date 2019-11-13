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
import javax.inject.Inject

/**
 * A class to manage the Android's soft keyboard (that means the software keyboard, not a physical one).
 */
class KeyboardManager @Inject constructor(private val context: Context) {

    private val inputMethodManager: InputMethodManager
        get() = getInputMethodManager(context)

    private fun getInputMethodManager(context: Context) =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    /** Forces the soft keyboard to show for a specific [editText]. */
    fun show(editText: EditText) = inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)

    /** Forces the soft keyboard to hide, meant to be called from inside a Fragment's [view]. */
    fun hide(view: View) = inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

    /** Forces the device's soft keyboard to hide, meant to be called from inside an [activity]. */
    fun hide(activity: Activity) {
        // If no view currently has focus, create a new one,
        // just so we can grab a window token from it
        val view = activity.currentFocus ?: View(activity)
        getInputMethodManager(activity).hideSoftInputFromWindow(view.windowToken, 0)
    }
}