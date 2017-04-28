package ar.com.wolox.wolmo.core.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import ar.com.wolox.wolmo.core.presenter.BasePresenter;

public interface IWoloxFragment<T extends BasePresenter> {

    /**
     * Returns the layout id for the inflater so the view can be populated
     */
    int layout();

    /**
     * Reads arguments sent as a Bundle and saves them as appropriate.
     * <b>NOTE: </b>Returning <i>false</i> will end the execution of the activity.
     *
     * @param args The bundle obtainable by the {@link Fragment#getArguments()} method.
     *
     * @return <b>true</b> if this fragment contains the required values, <b>false</b> otherwise.
     * Default implementation returns true.
     */
    boolean handleArguments(Bundle args);

    /**
     * Create the presenter for this fragment
     */
    T createPresenter();

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
