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
import ar.com.wolox.wolmo.core.util.ReflectionUtils;
import ar.com.wolox.wolmo.core.util.ToastUtils;

import java.lang.reflect.Type;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * This class is used to separate Wolox Fragments logic so that different subclasses of
 * Fragment can implement MVP without re-writing this.
 */
public final class WolmoFragmentHandler<T extends BasePresenter> {

    private static final String TAG = "WolmoFragmentHandler";

    private Fragment mFragment;
    private IWolmoFragment mWolmoFragment;

    private boolean mCreated;
    private boolean mMenuVisible;
    private boolean mVisible;
    private Unbinder mUnbinder;

    @Inject T mPresenter;
    @Inject ToastUtils mToastUtils;

    @Inject
    public WolmoFragmentHandler() {}

    /**
     * Sets the fragment for this fragment handler.
     *
     * @param wolmoFragment Wolox fragment to attach.
     */
    private void setFragment(@NonNull IWolmoFragment wolmoFragment) {
        if (!(wolmoFragment instanceof Fragment)) {
            throw new IllegalArgumentException("WolmoFragment should be a Fragment instance");
        }
        mFragment = (Fragment) wolmoFragment;
        mWolmoFragment = wolmoFragment;
    }

    /**
     * Method called from {@link WolmoFragment#onCreate(Bundle)}, it calls to {@link
     * WolmoFragment#handleArguments(Bundle)} to check if the fragment has the correct arguments.
     *
     * @param savedInstanceState Saved instance state
     */
    void onCreate(@NonNull IWolmoFragment wolmoFragment, @Nullable Bundle savedInstanceState) {
        setFragment(wolmoFragment);
        if (!mWolmoFragment.handleArguments(mFragment.getArguments())) {
            Log.e(TAG, mFragment.getClass().getSimpleName() +
                " - The fragment's handleArguments() returned false.");
            mToastUtils.show(R.string.unknown_error);
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

        View v = inflater.inflate(mWolmoFragment.layout(), container, false);
        mUnbinder = ButterKnife.bind(mFragment, v);
        return v;
    }

    /**
     * Method called from {@link WolmoFragment#onViewCreated(View, Bundle)}, it attaches the
     * fragment to the {@link BasePresenter} calling {@link BasePresenter#onViewAttached()}.
     */
    @SuppressWarnings("unchecked")
    void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mCreated = true;
        // We check if the fragment implements or extends the required type for the presenter's view.
        // If it does, we try to assign it as a view, otherwise we fail and no view will be available for the presenter
        if (getPresenter() != null) {
            try {
                Type[] viewTypes = ReflectionUtils.getParameterizedTypes(getPresenter());
                if (viewTypes != null && ReflectionUtils.getClass(viewTypes[0])
                    .isAssignableFrom(mWolmoFragment.getClass())) {
                    mPresenter.attachView(mWolmoFragment);
                }
            } catch (ClassNotFoundException | NullPointerException e) {
                Log.e(TAG, mFragment.getClass().getSimpleName() +
                    " - The fragment should implement the presenter type argument.");
                mToastUtils.show(R.string.unknown_error);
                mFragment.getActivity().finish();
            }
        }

        mWolmoFragment.setUi(view);
        mWolmoFragment.init();
        mWolmoFragment.populate();
        mWolmoFragment.setListeners();
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
            mWolmoFragment.onVisible();
            mVisible = true;
        } else if ((!mMenuVisible || !mFragment.isResumed()) && mVisible) {
            mWolmoFragment.onHide();
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
     * the view is destroyed, calling {@link BasePresenter#onViewDetached()}
     */
    void onDestroyView() {
        if (getPresenter() != null) {
            getPresenter().detachView();
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
