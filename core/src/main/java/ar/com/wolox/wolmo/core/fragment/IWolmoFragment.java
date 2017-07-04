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
package ar.com.wolox.wolmo.core.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * This interface defines a set of methods that compose the lifecycle of Wolmo's fragments
 */
public interface IWolmoFragment {

    /**
     * This method provides a way for populating the view with a layout defined in an XML resource.
     * <p>
     * Example:
     * protected abstract int layout() {
     * return R.layout.my_layout_for_this_activity;
     * }
     *
     * @return The ID of the layout defined in an XML that will be used for populating the view.
     */
    @LayoutRes
    int layout();

    /**
     * Reads arguments sent as a Bundle extras.
     * <b>NOTE: </b>Returning <i>false</i> will end the execution of the fragment.
     *
     * @param args The bundle obtainable by the {@link Fragment#getArguments()} method.
     * @return <b>true</b> if this fragment contains the required values, <b>false</b> otherwise.
     * Default implementation returns true.
     */
    boolean handleArguments(Bundle args);

    /**
     * Loads the view elements for the fragment
     */
    void setUi(View v);

    /**
     * Initializes any variables that the fragment needs
     */
    void init();

    /**
     * Populates the view elements of the fragment
     */
    void populate();

    /**
     * Sets the listeners for the views of the fragment
     */
    void setListeners();

    /**
     * Override this method is you want to do anything when the fragment becomes visible
     */
    void onVisible();

    /**
     * Override this method is you want to do anything when the fragment hides
     */
    void onHide();

    /**
     * Custom back press handling method.
     *
     * @return 'true' if the back was handled and shouldn't propagate, 'false' otherwise
     */
    boolean onBackPressed();
}
