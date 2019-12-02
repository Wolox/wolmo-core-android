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
package ar.com.wolox.wolmo.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import ar.com.wolox.wolmo.core.permission.PermissionManager
import ar.com.wolox.wolmo.core.presenter.BasePresenter
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Base implementation for [IWolmoFragment]. This is in charge of inflating the view returned
 * by [layout]. The presenter is created on [onCreate] if [handleArguments] returns true.
 * This class defines default implementations for most of the methods on [IWolmoFragment].
 */
abstract class WolmoFragment<V : Any, P : BasePresenter<V>> : DaggerFragment(), IWolmoFragment {

    @Inject
    lateinit var fragmentHandler: WolmoFragmentHandler<V, P>

    @Inject
    lateinit var permissionManager: PermissionManager

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHandler.onCreate(this)
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return fragmentHandler.onCreateView(inflater, container)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHandler.onViewCreated()
    }

    @CallSuper
    override fun setMenuVisibility(visible: Boolean) {
        super.setMenuVisibility(visible)
        fragmentHandler.setMenuVisibility(visible)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        fragmentHandler.onResume()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        fragmentHandler.onPause()
    }

    @CallSuper
    override fun onDestroyView() {
        fragmentHandler.onDestroyView()
        super.onDestroyView()
    }

    /** Delegates permission handling to Wolmo's [PermissionManager]. */
    @CallSuper
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Reads [arguments] sent as a Bundle extras and returns true if arguments
     * were read successfully, false otherwise.
     *
     * Default implementation returns true.
     */
    override fun handleArguments(arguments: Bundle?): Boolean {
        return true
    }

    /**
     * Sets the listeners for the views of the fragment.
     * Override if needed.
     */
    override fun setListeners() {}

    /**
     * Populates the view elements of the fragment.
     * Override if needed.
     */
    override fun populate() {}

    /**
     * Invoked when the fragment becomes visible to the user.
     * Override if needed.
     */
    override fun onVisible() {}

    /**
     * Invoked when the fragment becomes hidden to the user.
     * Override if needed.
     */
    override fun onHide() {}

    /** @see IWolmoFragment.onBackPressed */
    override fun onBackPressed(): Boolean {
        return false
    }

    /**
     * Tries to return a non null instance of the presenter [P] for this fragment.
     * If the presenter is null this will throw a NullPointerException.
     */
    fun requirePresenter() = fragmentHandler.requirePresenter()
}