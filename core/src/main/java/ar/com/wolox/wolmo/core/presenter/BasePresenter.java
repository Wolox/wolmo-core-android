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
import android.support.annotation.NonNull;

/**
 * Base presenter that provides the view to the specific presenters.
 */
public class BasePresenter<T> {

    private final T mViewInstance;
    private boolean mViewCreated;
    private boolean mViewAttached;

    public BasePresenter(@NonNull T viewInstance) {
        mViewInstance = viewInstance;
        mViewAttached = true;
    }

    /**
     * Returns the view attached this presenter.
     * <b>NOTE: </b> You should check if the view is created, or at least attached calling the
     * methods {@link BasePresenter#isViewCreated()} and
     * {@link BasePresenter#isViewAttached()} before calling any method of the view.
     * It's not safe to call methods if the view isn't created and you should not call methods
     * if the view is not attached.
     *
     * @return Attached view
     */
    @NonNull
    protected final T getView() {
        return mViewInstance;
    }

    /**
     * Returns the state of the presenter's view.
     *
     * @return <b>true</b> if the view is attached, <b>false</b> otherwise.
     */
    public final boolean isViewAttached() {
        return mViewAttached;
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
     * Method called to detach the view of this presenter.
     */
    @CallSuper
    public void detachView() {
        mViewAttached = false;
        mViewCreated = false;
    }

    /**
     * Method called when the view is created and it's safe for the presenter to interact with it.
     * If the view is not attached this doesn't have any effect, because you shouldn't have a
     * view created but not attached.
     */
    @CallSuper
    public void onViewCreated() {
        mViewCreated = mViewAttached;
    }

    /**
     * Method called when the view is destroyed and it's no longer safe for the presenter to
     * interact with it.
     */
    @CallSuper
    public void onViewDestroyed() {
        mViewCreated = false;
    }

    /**
     * Runs the {@link Consumer<T>} if the view is attached to the presenter and created.
     * If the view is not created, the expression will not run.
     * The consumer receives the view instance as argument.
     *
     * @param method Expression to run if the view is created.
     */
    protected void runIfViewCreated(Consumer<T> method) {
        if (isViewCreated()) {
            method.accept(mViewInstance);
        }
    }

    /**
     * Runs the {@link {@link Runnable}} if the view is attached to the presenter and created.
     * If the view is not created, the expression will not run.
     * The runnable will not run in another thread.
     *
     * @param method Expression to run if the view is created.
     */
    protected void runIfViewCreated(Runnable method) {
        if (isViewCreated()) {
            method.run();
        }
    }

    /**
     * Functional interface to define a consumer method.
     * A consumer is a interface with only a method that accepts a parameter and does not return anything.
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