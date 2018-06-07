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

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import ar.com.wolox.wolmo.core.permission.PermissionManager;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/**
 * Base implementation for {@link IWolmoFragment}. This is in charge of inflating the view returned
 * by {@link #layout()} and calls {@link ButterKnife} to bind members. The presenter is created on
 * {@link #onCreate(Bundle)} if {@link #handleArguments(Bundle)} returns true.
 * This class defines default implementations for most of the methods on {@link IWolmoFragment}.
 *
 * @param <T> Presenter for this fragment. It should extend {@link BasePresenter}
 */
public abstract class WolmoFragment<T extends BasePresenter> extends DaggerFragment
        implements IWolmoFragment {

    @Inject WolmoFragmentHandler<T> mFragmentHandler;
    @Inject PermissionManager mPermissionManager;

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentHandler.onCreate(this, savedInstanceState);
    }

    @Override
    @CallSuper
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return mFragmentHandler.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    @CallSuper
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentHandler.onViewCreated(view, savedInstanceState);
    }

    @Override
    @CallSuper
    public void setMenuVisibility(boolean visible) {
        super.setMenuVisibility(visible);
        mFragmentHandler.setMenuVisibility(visible);
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        mFragmentHandler.onResume();
    }

    @Override
    @CallSuper
    public void onPause() {
        super.onPause();
        mFragmentHandler.onPause();
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        mFragmentHandler.onDestroyView();
        super.onDestroyView();
    }

    /**
     * Delegates permission handling to Wolmo's {@link PermissionManager}.
     */
    @Override
    @CallSuper
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        mPermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Reads arguments sent as a Bundle extras.
     *
     * @param arguments The bundle obtainable by the getExtras method of the intent.
     *
     * @return true if arguments were read successfully, false otherwise.
     * Default implementation returns true.
     */
    @Override
    public boolean handleArguments(@Nullable Bundle arguments) {
        return true;
    }

    /**
     * Associates variables to views inflated from the XML resource
     * provided in {@link IWolmoFragment#layout()}
     * Override if needed. If using {@link ButterKnife}, there is no need to use this method.
     */
    @Override
    public void setUi(View v) {}

    /**
     * Sets the listeners for the views of the fragment.
     * Override if needed.
     */
    @Override
    public void setListeners() {}

    /**
     * Populates the view elements of the fragment.
     * Override if needed.
     */
    @Override
    public void populate() {}

    /**
     * Callback called when the fragment becomes visible to the user.
     * Override if needed.
     */
    @Override
    public void onVisible() {}

    /**
     * Callback called when the fragment becomes hidden to the user.
     * Override if needed.
     */
    @Override
    public void onHide() {}

    /**
     * Returns the instance of the presenter for this fragment.
     *
     * @return Presenter for this fragment
     */
    protected T getPresenter() {
        return mFragmentHandler.getPresenter();
    }

    /**
     * Tries to return a non null instance of the presenter {@link T} for this fragment.
     * If the presenter is null this will throw a NullPointerException.
     *
     * @return presenter
     */
    @NonNull
    T requirePresenter() {
        return mFragmentHandler.requirePresenter();
    }

    /**
     * @see IWolmoFragment#onBackPressed()
     * <p>
     * Beware, when overriding, that returning 'true' will prevent default navigation behaviour
     * such
     * as {@link FragmentManager#popBackStackImmediate()} or {@link Activity#finish()}, but not
     * dismissing the keyboard, for example.
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }
}