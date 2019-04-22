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
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import javax.inject.Inject;

import ar.com.wolox.wolmo.core.permission.PermissionManager;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatDialogFragment;

/**
 * Base implementation for {@link IWolmoFragment} for dialog fragments. This is in charge of
 * inflating the view returned by {@link #layout()} and calls {@link ButterKnife} to bind members.
 * The presenter is created on {@link #onCreate(Bundle)} if {@link #handleArguments(Bundle)} returns
 * true. This class defines default implementations for most of the methods on {@link
 * IWolmoFragment}.
 *
 * @param <T> Presenter for this fragment. It should extend {@link BasePresenter}
 */
public abstract class WolmoDialogFragment<T extends BasePresenter>
        extends DaggerAppCompatDialogFragment implements IWolmoFragment {

    @Inject WolmoFragmentHandler<T> mFragmentHandler;
    @Inject PermissionManager mPermissionManager;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            Drawable backgroundDrawable = getBackgroundDrawable();
            if (backgroundDrawable != null) {
                getDialog().getWindow().setBackgroundDrawable(backgroundDrawable);
            }
            setOnBackPressedListener();
        }
    }

    /**
     * Returns a {@link Drawable} for use as background in the window.
     * If you want to disable the background drawable return null.
     * By default this method returns the color #01FFFFFF
     *
     * @return Background drawable
     */
    @Nullable
    protected Drawable getBackgroundDrawable() {
        return new ColorDrawable(Color.argb(1, 255, 255, 255));
    }

    /**
     * Sets a custom {@link android.content.DialogInterface.OnKeyListener} for the
     * {@link Dialog} returned by {@link #getDialog()} that calls {@link #onBackPressed()}
     * if the key pressed is the back key.
     * <p>
     * Beware that, when clicking a key, the {@link android.content.DialogInterface.OnKeyListener}
     * is called before delegating the event to other structures. For example, the back is handled
     * here before sending it to an {@link Activity}.
     */
    private void setOnBackPressedListener() {
        if (getDialog() == null) return;
        getDialog().setOnKeyListener(
                (dialog, keyCode, event) -> keyCode == KeyEvent.KEYCODE_BACK && onBackPressed());
    }

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
    public void setMenuVisibility(final boolean visible) {
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
     * Reads arguments sent as a Bundle extras.
     *
     * @param arguments The bundle obtainable by the getExtras method of the intent.
     *
     * @return true if arguments were read successfully, false otherwise.
     * Default implementation returns true.
     */
    public boolean handleArguments(@Nullable Bundle arguments) {
        return true;
    }

    /**
     * Associates variables to views inflated from the XML resource
     * provided in {@link IWolmoFragment#layout()}
     * Override if needed. If using {@link ButterKnife}, there is no need to use this method.
     */
    public void setUi(@NonNull View v) {}

    /**
     * Sets the listeners for the views of the fragment.
     * Override if needed.
     */
    public void setListeners() {}

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
     * Populates the view elements of the fragment.
     * Override if needed.
     */
    @Override
    public void populate() {}

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
     * Shows the {@link WolmoDialogFragment} using the fragment.
     *
     * @param manager Fragment Manager to show the dialog fragment
     */
    public void show(@NonNull FragmentManager manager) {
        super.show(manager, getClass().getName());
    }

    /**
     * @see IWolmoFragment#onBackPressed()
     * <p>
     * Beware, when overriding, that returning 'true' will prevent default navigation behaviour
     * such
     * as {@link Dialog#dismiss()}.
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        mPermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}