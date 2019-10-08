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
package ar.com.wolox.wolmo.core.presenter

import androidx.core.util.Consumer
import javax.inject.Inject

/**
 * Base presenter that provides the view to the specific presenters.
 */
@Suppress("DeprecatedCallableAddReplaceWith")
open class BasePresenter<V> @Inject constructor() {

    /**
     * Returns the [view] attached this presenter.
     * **NOTE: ** You should check if the view is attached calling the
     * method (before calling any method of the view).[BasePresenter.isViewAttached]
     */
    var view: V? = null
        private set(value) {
            field = value
        }

    /** Returns **true** if the [view] is attached, **false** otherwise. */
    fun isViewAttached(): Boolean = view != null

    /**
     * Call this method to attach a given [viewInstance] to this presenter.
     * The attached view should be ready to interact with it. This method calls [onViewAttached]
     * to notify subclasses that a view was attached.
     */
    fun attachView(viewInstance: V) {
        view = viewInstance
        onViewAttached()
    }

    /**
     * Method called to detach the [view] of this presenter.
     * This method calls [onViewDetached] to notify subclasses.
     */
    fun detachView() {
        view = null
        onViewDetached()
    }

    /**  Listener called when a view is attached and it's safe to interact with it. */
    fun onViewAttached() {}

    /**
     * Method called when the view is destroyed and it's no longer safe for the presenter to
     * interact with it.
     */
    fun onViewDetached() {}

    /**
     * Runs the [method] if the [view] is attached to the presenter.
     * If the [view] is not attached, the expression will not run.
     * The consumer receives the view instance as argument.
     */
    @Deprecated(message = "Since Kotlin allows safe calls (?.), this method is unnecessary. Use view?")
    fun runIfViewAttached(method: Consumer<V>) {
        if (isViewAttached()) {
            method.accept(view)
        }
    }

    /**
     * Runs the [method] if the [view] is attached to the presenter.
     * If the [view] is not attached, the expression will not run.
     * The runnable will not run in another thread.
     */
    @Deprecated(message = "Since Kotlin allows safe calls **(?.)**, this method is unnecessary. Use view?")
    fun runIfViewAttached(method: Runnable) {
        if (isViewAttached()) {
            method.run()
        }
    }
}
