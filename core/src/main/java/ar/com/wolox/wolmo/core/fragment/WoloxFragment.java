package ar.com.wolox.wolmo.core.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ar.com.wolox.wolmo.core.R;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;
import butterknife.ButterKnife;

public abstract class WoloxFragment<T extends BasePresenter> extends Fragment {

    protected T mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(layout(), container, false);
        ButterKnife.bind(this, v);
        if (handleArguments(getArguments())) {
            mPresenter = createPresenter();
            setUi(v);
            init();
            populate();
            setListeners();
        } else {
            showToast(R.string.unknown_error);
            getActivity().finish();
        }
        return v;
    }

    /**
     * Returns the layout id for the inflater so the view can be populated
     */
    protected abstract int layout();

    /**
     * Reads arguments sent as a Bundle and saves them as appropriate.
     * @param args The bundle obtainable by the getArguments method.
     * @return true if arguments were read successfully, false otherwise.
     * Default implementation returns true.
     */
    protected boolean handleArguments(Bundle args) {
        return true;
    }

    /**
     * Create the presenter for this fragment
     */
    protected abstract T createPresenter();

    /**
     * Loads the view elements for the fragment
     */
    protected void setUi(View v) {
        // Do nothing, ButterKnife does this for us now!
    }

    /**
     * Initializes any variables that the fragment needs
     */
    protected abstract void init();

    /**
     * Populates the view elements of the fragment
     */
    protected void populate() {
        // Do nothing, override if needed!
    }

    /**
     * Sets the listeners for the views of the fragment
     */
    protected void setListeners() {
        // Do nothing, override if needed!
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    protected void showToast(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    protected void replaceFragment(int resId, Fragment f) {
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(resId, f)
                .commit();
    }
}