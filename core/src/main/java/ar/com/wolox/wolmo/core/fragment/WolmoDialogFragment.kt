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

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import ar.com.wolox.wolmo.core.permission.PermissionManager
import ar.com.wolox.wolmo.core.presenter.BasePresenter
import dagger.android.support.DaggerAppCompatDialogFragment
import javax.inject.Inject

/**
 * Base implementation for [IWolmoFragment] for dialog fragments. This is in charge of
 * inflating the view returned by [layout].
 * The presenter is created on [onCreate] if [handleArguments] returns true.
 */
abstract class WolmoDialogFragment<V : ViewDataBinding, P : BasePresenter<*>> : DaggerAppCompatDialogFragment(),
        IWolmoFragment {

    @Inject
    lateinit var permissionManager: PermissionManager

    @Inject
    lateinit var fragmentHandler: WolmoFragmentHandler<V, P>

    val presenter: P
        get() = fragmentHandler.presenter

    val binding: V?
        get() = fragmentHandler.binding

    override fun onCreateDialog(savedInstanceState: Bundle?) = super.onCreateDialog(savedInstanceState).apply {
        window?.run {
            requestFeature(Window.FEATURE_NO_TITLE)
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.run {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(backgroundDrawable)
            setOnBackPressedListener()
        }
    }

    /**
     * Returns a [Drawable] for use as background in the window.
     * If you want to disable the background drawable return null.
     * By default this method returns the color #01FFFFFF
     */
    open val backgroundDrawable: Drawable
        get() = ColorDrawable(Color.argb(1, 255, 255, 255))

    /**
     * Sets a custom [android.content.DialogInterface.OnKeyListener] for the
     * [Dialog] returned by [.getDialog] that calls [.onBackPressed]
     * if the key pressed is the back key.
     *
     * Beware that, when clicking a key, the [android.content.DialogInterface.OnKeyListener]
     * is called before delegating the event to other structures. For example, the back is handled
     * here before sending it to an [Activity].
     */
    open fun setOnBackPressedListener() {
        dialog?.setOnKeyListener { _: DialogInterface?, keyCode: Int, _: KeyEvent? ->
            keyCode == KeyEvent.KEYCODE_BACK && onBackPressed()
        }
    }

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
        fragmentHandler.onViewCreated(view)
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

    /**
     * Reads arguments sent as a Bundle extras.
     *
     * @param arguments The bundle obtainable by the getExtras method of the intent.
     *
     * @return true if arguments were read successfully, false otherwise.
     * Default implementation returns true.
     */
    override fun handleArguments(arguments: Bundle?) = true

    /**
     * Associates variables to views inflated from the XML resource
     * provided in [IWolmoFragment.layout]
     * Override if needed.
     */
    override fun setUi(view: View?) {}

    /**
     * Sets the listeners for the views of the fragment.
     * Override if needed.
     */
    override fun setListeners() {}

    /**
     * Callback called when the fragment becomes visible to the user.
     * Override if needed.
     */
    override fun onVisible() {}

    /**
     * Callback called when the fragment becomes hidden to the user.
     * Override if needed.
     */
    override fun onHide() {}

    /**
     * Populates the view elements of the fragment.
     * Override if needed.
     */
    override fun populate() {}

    /**
     * Shows the [WolmoDialogFragment] using the fragment.
     *
     * @param manager Fragment Manager to show the dialog fragment
     */
    fun show(manager: FragmentManager) {
        super.show(manager, javaClass.name)
    }

    /**
     * @see IWolmoFragment.onBackPressed
     */
    override fun onBackPressed(): Boolean {
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}