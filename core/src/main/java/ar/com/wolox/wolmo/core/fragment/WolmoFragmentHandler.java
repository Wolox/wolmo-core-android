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
package ar.com.wolox.wolmo.core.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.com.wolox.wolmo.core.R;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;
import ar.com.wolox.wolmo.core.util.ToastUtils;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * This class is used to separate Wolox Fragments logic so that different subclasses of
 * Fragment can implement MVP without re-writing this.
 */
class WolmoFragmentHandler<T extends BasePresenter> {

    private static final String TAG = "WolmoFragmentHandler";

    private final Fragment mFragment;
    private final IWolmoFragment<T> mWoloxFragment;
    private boolean mCreated;
    private T mPresenter;
    private boolean mMenuVisible;
    private boolean mVisible;
    private Unbinder mUnbinder;

    WolmoFragmentHandler(@NonNull IWolmoFragment<T> woloxFragment) {
        if (!(woloxFragment instanceof Fragment)) {
            throw new IllegalArgumentException("WolmoFragment should be a Fragment instance");
        }
        mFragment = (Fragment) woloxFragment;
        mWoloxFragment = woloxFragment;
    }

    /**
     * Method called from {@link WolmoFragment#onCreate(Bundle)}, it calls to {@link
     * WolmoFragment#handleArguments(Bundle)}
     * to check if the fragment has the correct arguments and creates a presenter calling {@link
     * WolmoFragment#createPresenter()}.
     *
     * @param savedInstanceState Saved instance state
     */
    void onCreate(@Nullable Bundle savedInstanceState) {
        if (mWoloxFragment.handleArguments(mFragment.getArguments())) {
            mPresenter = mWoloxFragment.createPresenter();
        } else {
            Log.e(TAG, mFragment.getClass().getSimpleName() +
                    " - The fragment's handleArguments returned false.");
            ToastUtils.show(R.string.unknown_error);
            mFragment.getActivity().finish();
        }
    }

    /**
     * Method called from {@link WolmoFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}, it
     * creates the view defined in
     * {@link WolmoFragment#layout()} and binds it with the fragment with {@link ButterKnife}.
     * Then it calls the following methods and returns the {@link View} created:
     * <p><ul>
     * <li>{@link WolmoFragment#setUi(View)}
     * <li>{@link WolmoFragment#init()}
     * <li>{@link WolmoFragment#populate()}
     * <li>{@link WolmoFragment#setListeners()}
     * </ul><p>
     */
    View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                      @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(mWoloxFragment.layout(), container, false);
        mUnbinder = ButterKnife.bind(mFragment, v);
        mWoloxFragment.setUi(v);
        mWoloxFragment.init();
        mWoloxFragment.populate();
        mWoloxFragment.setListeners();

        mCreated = true;
        return v;
    }

    /**
     * Method called from {@link WolmoFragment#onViewCreated(View, Bundle)}, it notifies the {@link
     * BasePresenter} that the view was created calling {@link BasePresenter#onViewCreated()}.
     */
    void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getPresenter() != null) {
            getPresenter().onViewCreated();
        }
    }

    /**
     * Returns the presenter {@link T} for this fragment
     *
     * @return presenter
     */
    @Nullable
    T getPresenter() {
        return mPresenter;
    }

    private void onVisibilityChanged() {
        if (!mCreated) return;
        if (mFragment.isResumed() && mMenuVisible && !mVisible) {
            mWoloxFragment.onVisible();
            mVisible = true;
        } else if ((!mMenuVisible || !mFragment.isResumed()) && mVisible) {
            mWoloxFragment.onHide();
            mVisible = false;
        }
    }

    /**
     * Called from {@link WolmoFragment#onResume()}, checks visibility of the fragment
     * and calls {@link WolmoFragment#onVisible()} or {@link WolmoFragment#onHide()} accordingly.
     */
    void onResume() {
        onVisibilityChanged();
    }

    /**
     * Called from {@link WolmoFragment#onPause()}, checks visibility of the fragment
     * and calls {@link WolmoFragment#onVisible()} or {@link WolmoFragment#onHide()} accordingly.
     */
    void onPause() {
        onVisibilityChanged();
    }

    /**
     * Called from {@link WolmoFragment#setMenuVisibility(boolean)}, checks visibility of the
     * fragment's menu and calls {@link WolmoFragment#onVisible()} or
     * {@link WolmoFragment#onHide()} accordingly.
     * For mor info {@see Fragment#setMenuVisibility(boolean)}.
     */
    void setMenuVisibility(boolean visible) {
        mMenuVisible = visible;
        onVisibilityChanged();
    }

    /**
     * Called from {@link WolmoFragment#onDestroyView()}, it notifies the {@link BasePresenter} that
     * the view is destroyed, calling {@link BasePresenter#onViewDestroyed()}
     */
    void onDestroyView() {
        if (getPresenter() != null) {
            getPresenter().onViewDestroyed();
        }
        mUnbinder.unbind();
    }

    /**
     * Called from {@link WolmoFragment#onDestroy()}. It calls {@link
     * BasePresenter#detachView()}
     */
    void onDestroy() {
        if (getPresenter() != null) getPresenter().detachView();
    }
}
