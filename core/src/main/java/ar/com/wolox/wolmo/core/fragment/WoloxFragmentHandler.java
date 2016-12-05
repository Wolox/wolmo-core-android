package ar.com.wolox.wolmo.core.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.com.wolox.wolmo.core.R;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;
import ar.com.wolox.wolmo.core.util.ToastUtils;
import butterknife.ButterKnife;

/**
 * This class is used to separate Wolox Fragments logic so that different subclasses of
 * Fragment can implement MVP without re-writing this.
 */

class WoloxFragmentHandler<T extends BasePresenter> {

    private final Fragment mFragment;
    private final IWoloxFragment<T> mWoloxFragment;
    private boolean mCreated;
    private T mPresenter;
    private boolean mMenuVisible;
    private boolean mVisible;

    WoloxFragmentHandler(IWoloxFragment<T> woloxFragment) {
        if(!(woloxFragment instanceof Fragment)) {
            throw new IllegalArgumentException("WoloxFragment should be a Fragment instance");
        }
        mFragment = (Fragment) woloxFragment;
        mWoloxFragment = woloxFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(mWoloxFragment.layout(), container, false);
        ButterKnife.bind(mFragment, v);
        if (mWoloxFragment.handleArguments(mFragment.getArguments())) {
            mPresenter = mWoloxFragment.createPresenter();
            mWoloxFragment.setUi(v);
            mWoloxFragment.init();
            mWoloxFragment.populate();
            mWoloxFragment.setListeners();
        } else {
            ToastUtils.showToast(R.string.unknown_error);
            mFragment.getActivity().finish();
        }
        return v;
    }

    T getPresenter() {
        return mPresenter;
    }

    private void onVisibilityChanged() {
        if(!mCreated) return;
        if(mFragment.isResumed() && mMenuVisible && !mVisible) {
            mWoloxFragment.onVisible();
            mVisible = true;
        } else if((!mMenuVisible || !mFragment.isResumed()) && mVisible) {
            mWoloxFragment.onHide();
            mVisible = false;
        }
    }

    void onResume() {
        onVisibilityChanged();
    }

    void onPause() {
        onVisibilityChanged();
    }

    void setMenuVisibility(boolean visible) {
        mMenuVisible = visible;
        onVisibilityChanged();
    }


    void onDestroyView() {
        if (getPresenter() != null) getPresenter().detachView();
    }
}
