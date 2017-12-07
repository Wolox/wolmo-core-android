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

import javax.inject.Inject;

/**
 * Base presenter that provides the view to the specific presenters.
 */
public class BasePresenter<V> {

    private V mViewInstance;

    @Inject
    public BasePresenter() {}

    /**
     * Returns the view attached this presenter.
     * <b>NOTE: </b> You should check if the view is attached calling the
     * method {@link BasePresenter#isViewAttached before calling any method of the view.
     *
     * @return Attached view, if any. Otherwise return null.
     */
    protected final V getView() {
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
     * Call this method to attach a view to this presenter.
     * The attached view should be ready to interact with it. This method calls {@link
     * #onViewAttached()}
     * to notify subclasses that a view was attached.
     *
     * @param viewInstance Instance of the view to attach
     */
    public final void attachView(V viewInstance) {
        mViewInstance = viewInstance;
        onViewAttached();
    }

    /**
     * Method called to detach the view of this presenter.
     * This method calls {@link #onViewDetached()} to notify subclasses.
     */
    public final void detachView() {
        mViewInstance = null;
        onViewDetached();
    }

    /**
     * Listener called when a view is attached and it's safe to interact with it.
     */
    public void onViewAttached() {}

    /**
     * Method called when the view is destroyed and it's no longer safe for the presenter to
     * interact with it.
     */
    public void onViewDetached() {}

    /**
     * Runs the {@link Consumer<V>} if the view is attached to the presenter.
     * If the view is not attached, the expression will not run.
     * The consumer receives the view instance as argument.
     *
     * @param method Expression to run if the view is created.
     */
    protected void runIfViewAttached(Consumer<V> method) {
        if (isViewAttached()) {
            method.accept(mViewInstance);
        }
    }

    /**
     * Runs the {@link {@link Runnable}} if the view is attached to the presenter.
     * If the view is not attached, the expression will not run.
     * The runnable will not run in another thread.
     *
     * @param method Expression to run if the view is created.
     */
    protected void runIfViewAttached(Runnable method) {
        if (isViewAttached()) {
            method.run();
        }
    }

    /**
     * Functional interface to define a consumer method.
     * A consumer is a interface with only a method that accepts a parameter and does not return
     * anything.
     *
     * @param <T> Parameter
     */
    public interface Consumer<T> {

        /**
         * Accept the argument
         *
         * @param param argument
         */
        void accept(T param);
    }
}