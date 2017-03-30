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
        mViewInstance = null;
        mViewCreated = false;
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