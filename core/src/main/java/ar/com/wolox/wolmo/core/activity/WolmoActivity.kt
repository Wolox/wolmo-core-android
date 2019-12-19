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
package ar.com.wolox.wolmo.core.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import ar.com.wolox.wolmo.core.fragment.IWolmoFragment
import ar.com.wolox.wolmo.core.permission.PermissionManager
import ar.com.wolox.wolmo.core.util.ToastFactory
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

/**
 * A base [DaggerAppCompatActivity] that implements Wolmo's custom lifecycle.
 */
abstract class WolmoActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var toastFactory: ToastFactory
    @Inject
    lateinit var permissionManager: PermissionManager

    /**
     * Handles the custom lifecycle of Wolmo's Activity. It provides a set of callbacks to structure
     * the different aspects of the Activities initialization.
     */
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout())
        if (handleArguments(intent.extras)) {
            init()
            populate()
            setListeners()
        } else {
            toastFactory.show(ar.com.wolox.wolmo.core.R.string.unknown_error)
            finish()
        }
    }

    /**
     * This method provides a way for populating the view with a layout defined in an XML resource
     * by returning the ID of the layout
     *
     * Example:
     * override fun layout() = R.layout.my_layout_for_this_activity;
     */
    @LayoutRes
    protected abstract fun layout(): Int

    /**
     * Reads [arguments] and returns true if arguments were read successfully, false otherwise.
     * Default implementation returns true.
     */
    protected open fun handleArguments(arguments: Bundle?) = true

    /**
     * Associates variables to views inflated from the XML resource
     * provided in [WolmoActivity.layout]
     * Override if needed.
     */
    @Deprecated("Use kotlin view binding. This'll not be invoked.")
    protected open fun setUi() {
    }

    /**
     * Entry point for the Activity's specific code.
     * Prefer this callback over [android.app.Activity.onCreate].
     * Initializes any variables or behaviour that the activity needs.
     * Override if needed.
     */
    protected open fun init() {}

    /**
     * Populates the view elements of the activity.
     * Override if needed.
     */
    protected open fun populate() {}

    /**
     * Sets the listeners for the views of the activity.
     * Override if needed.
     */
    protected open fun setListeners() {}

    /**
     * Replaces the current [Fragment] in a given container layout defined by a [resId]
     * with a new [fragment].
     */
    // TODO We should delegate this methods to a helper
    protected fun replaceFragment(@IdRes resId: Int, fragment: Fragment) = supportFragmentManager.commit {
        replace(resId, fragment)
    }

    /**
     * Replaces the current [Fragment] in a given container layout defined by a [resId]
     * with a new [fragment] using a custom [tag] that allows the fragment to be more
     * easily located.
     */
    // TODO We should delegate this methods to a helper
    protected fun replaceFragment(@IdRes resId: Int, fragment: Fragment, tag: String) = supportFragmentManager.commit {
        replace(resId, fragment, tag)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    /** Delegates permission handling to Wolmo's [PermissionManager]. */
    @CallSuper
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Custom behaviour that calls, for every child fragment that is an instance of
     * [IWolmoFragment] and [Fragment.isVisible], its
     * [IWolmoFragment.onBackPressed].
     *
     *
     * If any of those returns 'true', the method returns. Else, it calls
     * [AppCompatActivity.onBackPressed].
     */
    @CallSuper
    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is IWolmoFragment && it.isVisible && it.onBackPressed()) {
                return
            }
        }
        super.onBackPressed()
    }
}