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
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import ar.com.wolox.wolmo.core.permission.PermissionManager;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;

public abstract class WoloxDialogFragment<T extends BasePresenter> extends DialogFragment
        implements IWoloxFragment<T> {
    private WoloxFragmentHandler<T> mFragmentHandler;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            Drawable backgroundDrawable = new ColorDrawable(Color.argb(1, 255, 0, 0));
            getDialog().getWindow().setBackgroundDrawable(backgroundDrawable);
            setOnBackPressedListener();
        }
    }

    /**
     * Sets a custom {@link android.content.DialogInterface.OnKeyListener} for the
     * {@link Dialog} returned by {@link #getDialog()} that calls {@link #onBackPressed()}
     * if the key is the back key.
     *
     * Beware that, when clicking a key, the {@link android.content.DialogInterface.OnKeyListener}
     * is called before delegating the event to other structures. For example, the back is handled
     * here before sending it to an {@link Activity}.
     */
    private void setOnBackPressedListener() {
        if (getDialog() == null) return;
        getDialog().setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK && onBackPressed();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentHandler = new WoloxFragmentHandler<T>(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return mFragmentHandler.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        mFragmentHandler.setMenuVisibility(visible);
    }

    @Override
    public void onResume() {
        super.onResume();
        mFragmentHandler.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mFragmentHandler.onPause();
    }

    @Override
    public void onDestroyView() {
        mFragmentHandler.onDestroyView();
        super.onDestroyView();
    }

    public boolean handleArguments(Bundle arguments) {
        return true;
    }

    public void setUi(View v) {
    }

    public void setListeners() {
    }

    @Override
    public void onVisible() {
    }

    @Override
    public void onHide() {
    }

    @Override
    public void populate() {

    }

    protected T getPresenter() {
        return mFragmentHandler.getPresenter();
    }

    public void show(FragmentManager manager) {
        super.show(manager, getClass().getName());
    }

    /**
     * @see IWoloxFragment#onBackPressed()
     *
     * Beware, when overriding, that returning 'true' will prevent default navigation behaviour such
     * as {@link Dialog#dismiss()}.
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.getInstance()
                .onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}