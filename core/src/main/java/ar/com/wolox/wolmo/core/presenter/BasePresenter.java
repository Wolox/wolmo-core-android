package ar.com.wolox.wolmo.core.presenter;


/**
 * Abstract presenter that provides the view to the specific presenters.
 */
public class BasePresenter<T> {

    private T mViewInstance;

    public BasePresenter(T viewInstance) {
        this.mViewInstance = viewInstance;
    }

    protected T getView() {
        return mViewInstance;
    }

    public boolean isViewAttached() {
        return mViewInstance != null;
    }

    public void detachView() {
        mViewInstance = null;
    }
}