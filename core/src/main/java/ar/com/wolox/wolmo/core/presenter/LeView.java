package ar.com.wolox.wolmo.core.presenter;

/**
 * Interface to extend from the views that have a loading view along with an error one.
 */
public interface LeView {

    /**
     * Display a loading view while loading data in background.
     * <b>The loading view must have the id = R.id.loadingView</b>
     */
    void showLoading();


    /**
     * Show the error view.
     * <b>The error view must be a TextView with the id = R.id.errorView</b>
     *
     * @param e The Throwable that has caused this error
     */
    void showError(Throwable e);

}