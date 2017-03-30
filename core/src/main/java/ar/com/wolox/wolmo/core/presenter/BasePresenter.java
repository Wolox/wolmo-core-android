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
package ar.com.wolox.wolmo.core.presenter;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

/**
 * Base presenter that provides the view to the specific presenters.
 */
public class BasePresenter<T> {

    private T mViewInstance;
    private boolean mViewCreated;

    public BasePresenter(T viewInstance) {
        this.mViewInstance = viewInstance;
    }

    /**
     * Returns the view attached this presenter.
     * If no view is attached, returns null.
     *
     * @return View attached
     */
    @Nullable
    protected final T getView() {
        return mViewInstance;
    }

    /**
     * Returns the state of the presenter's view.
     *
     * @return <b>true</b> if the view is attached, <b>false</b> otherwise.
     */
    public final boolean isViewAttached() {
        return mViewInstance != null;
    }

    /**
     * Method called to detach the view of this presenter.
     */
    public final void detachView() {
        mViewInstance = null;
        mViewCreated = false;
    }

    /**
     * Returns the status of the attached view.
     * <b>NOTE: </b> The view can be attached but not yet created.
     *
     * @return <b>true</b> if the view is created, <b>false</b> otherwise.
     */
    public final boolean isViewCreated() {
        return mViewCreated;
    }

    /**
     * Method called when the view is created and it's safe for the presenter to interact with it.
     */
    @CallSuper
    public void onViewCreated() {
        mViewCreated = true;
    }

    /**
     * Method called when the view is destroyed and it's no longer safe for the presenter to
     * interact with it.
     */
    @CallSuper
    public void onViewDestroyed() {
        mViewCreated = false;
    }

}