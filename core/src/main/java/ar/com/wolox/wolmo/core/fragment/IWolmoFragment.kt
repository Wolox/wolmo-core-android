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
package ar.com.wolox.wolmo.core.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes

/**
 * This interface defines a set of methods that compose the lifecycle of Wolmo's fragments
 */
interface IWolmoFragment {

    /**
     * This method provides a way for populating the view with a [layout] defined in an XML
     * resource.
     *
     * Example:
     * fun layout() = R.layout.my_layout_for_this_activity
     */
    @LayoutRes
    fun layout(): Int

    /**
     * Reads [arguments] sent as a Bundle extras and returning true if this fragment contains
     * the required values, false otherwise. Returning false will end the execution.
     *
     * Default implementation returns true.
     */
    fun handleArguments(arguments: Bundle?): Boolean

    /** Loads the [view] elements for the fragment. */
    fun setUi(view: View?)

    /** Initializes any variables that the fragment needs. */
    fun init()

    /** Populates the view elements of the fragment. */
    fun populate()

    /**
     * Sets the listeners for the views of the fragment
     */
    fun setListeners()

    /** Override this method is you want to do anything when the fragment becomes visible. */
    fun onVisible()

    /** Override this method is you want to do anything when the fragment hides. */
    fun onHide()

    /**
     * Custom back press handling method. and returns true if the back was handled and shouldn't
     * propagate, false otherwise
     */
    fun onBackPressed(): Boolean
}