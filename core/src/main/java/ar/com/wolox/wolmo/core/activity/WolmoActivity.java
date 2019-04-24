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
package ar.com.wolox.wolmo.core.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.List;

import javax.inject.Inject;

import ar.com.wolox.wolmo.core.fragment.IWolmoFragment;
import ar.com.wolox.wolmo.core.permission.PermissionManager;
import ar.com.wolox.wolmo.core.util.ToastFactory;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * A base {@link AppCompatActivity} that implements Wolmo's custom lifecycle.
 */
public abstract class WolmoActivity extends DaggerAppCompatActivity {

    @Inject ToastFactory mToastFactory;
    @Inject PermissionManager mPermissionManager;
    @Inject WolmoActivityHandler mActivityHandler;

    /**
     * Handles the custom lifecycle of Wolmo's Activity. It provides a set of callbacks to structure
     * the different aspects of the Activities initialization.
     *
     * @param savedInstanceState a {@link Bundle} provided by Android's lifecycle.
     */
    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityHandler.onCreate(this, savedInstanceState);
    }

    /**
     * This method provides a way for populating the view with a layout defined in an XML resource.
     * <p>
     * Example:
     * protected abstract int layout() {
     * return R.layout.my_layout_for_this_activity;
     * }
     *
     * @return The ID of the layout defined in an XML that will be used for populating the view.
     */
    @LayoutRes
    protected abstract int layout();

    /**
     * Reads arguments sent as a Bundle and saves them as appropriate.
     *
     * @param args The bundle obtainable by the getExtras method of the intent.
     *
     * @return true if arguments were read successfully, false otherwise.
     * Default implementation returns true.
     */
    protected boolean handleArguments(@Nullable Bundle args) {
        return true;
    }

    /**
     * Associates variables to views inflated from the XML resource
     * provided in {@link WolmoActivity#layout()}
     * Override if needed.
     */
    protected void setUi() {}

    /**
     * Entry point for the Activity's specific code.
     * Prefer this callback over {@link android.app.Activity#onCreate(Bundle)}.
     * Initializes any variables or behaviour that the activity needs.
     */
    protected abstract void init();

    /**
     * Populates the view elements of the activity.
     * Override if needed.
     */
    protected void populate() {}

    /**
     * Sets the listeners for the views of the activity.
     * Override if needed.
     */
    protected void setListeners() {}

    /**
     * Replaces the current {@link Fragment} in a given container layout with a new {@link Fragment}
     *
     * @param resId The ID of the layout that holds the current {@link Fragment}. It should be the
     * same container that will be used for the new {@link Fragment}
     * @param f An instance of a {@link Fragment} that will replace the older one.
     */
    // TODO We should delegate this methods to a helper
    protected void replaceFragment(int resId, Fragment f) {
        getSupportFragmentManager().beginTransaction().replace(resId, f).commit();
    }

    /**
     * Replaces the current {@link Fragment} in a given container layout with a new {@link Fragment}
     * using a custom tag ({@link String}) that allows the fragment to be more easily located.
     *
     * @param resId The ID of the layout that holds the current {@link Fragment}. It should be the
     * same container that will be used for the new {@link Fragment}
     * @param f An instance of a {@link Fragment} that will replace the older one.
     * @param tag A {@link String} that will be used to identify the {@link Fragment}
     */
    // TODO We should delegate this methods to a helper
    protected void replaceFragment(int resId, Fragment f, String tag) {
        getSupportFragmentManager().beginTransaction().replace(resId, f, tag).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Delegates permission handling to Wolmo's {@link PermissionManager}.
     */
    @Override
    @CallSuper
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Custom behaviour that calls, for every child fragment that is an instance of
     * {@link IWolmoFragment} and {@link Fragment#isVisible()}, its
     * {@link IWolmoFragment#onBackPressed()}.
     * <p>
     * If any of those returns 'true', the method returns. Else, it calls
     * {@link AppCompatActivity#onBackPressed()}.
     */
    @Override
    @CallSuper
    @SuppressWarnings("RestrictedApi")
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        // If activity has no child fragments, the list is null
        if (fragments != null) {
            for (Fragment childFragment : fragments) {
                if (childFragment instanceof IWolmoFragment && childFragment.isVisible()) {
                    if (((IWolmoFragment) childFragment).onBackPressed()) return;
                }
            }
        }

        super.onBackPressed();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        mActivityHandler.onDestroy();
        super.onDestroy();
    }
}