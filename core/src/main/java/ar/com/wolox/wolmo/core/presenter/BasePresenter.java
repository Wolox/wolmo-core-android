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
     * {@link BasePresenter#isViewAttached()} before calling method of the view.
     * It's not safe to call methods if the view isn't created.
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