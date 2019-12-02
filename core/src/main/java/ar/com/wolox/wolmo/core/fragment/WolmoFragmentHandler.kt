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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ar.com.wolox.wolmo.core.R
import ar.com.wolox.wolmo.core.presenter.BasePresenter
import ar.com.wolox.wolmo.core.util.Logger
import ar.com.wolox.wolmo.core.util.ToastFactory
import javax.inject.Inject

/**
 * This class is used to separate Wolox Fragments logic so that different subclasses of
 * Fragment can implement MVP without re-writing this.
 */
class WolmoFragmentHandler<V : Any, P : BasePresenter<V>> @Inject @JvmOverloads constructor(
        private val toastFactory: ToastFactory,
        private val logger: Logger,
        val presenter: P? = null
) {

    private lateinit var fragment: Fragment
    private lateinit var wolmoFragment: IWolmoFragment
    private lateinit var wolmoView: V
    private var created = false
    private var menuVisible = false
    private var visible = false

    /** Sets the [IWolmoFragment] for this fragment handler. */
    private fun setFragment(wolmoFragment: IWolmoFragment) {
        require(wolmoFragment is Fragment) { "WolmoFragment should be a Fragment instance" }
        fragment = wolmoFragment
        @Suppress("UNCHECKED_CAST")
        wolmoView = wolmoFragment as V
        this.wolmoFragment = wolmoFragment
    }

    /**
     * Invoked on [wolmoFragment]'s [WolmoFragment.onCreate] and checks
     * if the fragment has the correct arguments.
     */
    fun onCreate(wolmoFragment: IWolmoFragment) {
        logger.tag = WolmoFragmentHandler::class.java.simpleName
        setFragment(wolmoFragment)
        if (!wolmoFragment.handleArguments(fragment.arguments)) {
            logger.e(fragment.javaClass.simpleName +
                    " - The fragment's handleArguments() returned false.")
            toastFactory.show(R.string.unknown_error)
            fragment.requireActivity().finish()
        }
    }

    /**
     * Method called from [WolmoFragment.onCreateView], it creates the view defined in [WolmoFragment.layout].
     * Then it use the [inflater] to inflate the view into the [container] and returns the [View] created.
     */
    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(wolmoFragment.layout(), container, false)
    }

    /**
     * Method called from [WolmoFragment.onViewCreated]. It attaches the
     * fragment to the [BasePresenter] calling [BasePresenter.onViewAttached].
     */
    fun onViewCreated() {
        created = true
        presenter?.attachView(wolmoView)
        with(wolmoFragment) {
            init()
            populate()
            setListeners()
        }
    }

    /**
     * Invoked from [WolmoFragment.onResume]. It checks visibility of the fragment
     * and calls [WolmoFragment.onVisible] or [WolmoFragment.onHide] accordingly.
     */
    fun onResume() {
        onVisibilityChanged()
    }

    /**
     * Invoked from [WolmoFragment.onPause]. It checks visibility of the fragment
     * and calls [WolmoFragment.onVisible] or [WolmoFragment.onHide] accordingly.
     */
    fun onPause() {
        onVisibilityChanged()
    }

    /**
     * Invoked from [WolmoFragment.setMenuVisibility]. It checks visibility of the
     * fragment's menu and calls [WolmoFragment.onVisible] or [WolmoFragment.onHide] accordingly.
     */
    fun setMenuVisibility(visible: Boolean) {
        menuVisible = visible
        onVisibilityChanged()
    }

    /**
     * Called from [WolmoFragment.onDestroyView]. It notifies the [BasePresenter] that
     * the view is destroyed, calling [BasePresenter.onViewDetached]
     */
    fun onDestroyView() = requirePresenter().detachView()

    /**
     * Tries to return a non null instance of the presenter [P] for this fragment.
     * If the presenter is null this will throw a NullPointerException.
     */
    fun requirePresenter() = presenter!!

    private fun onVisibilityChanged() {
        if (!created) return
        if (fragment.isResumed && menuVisible && !visible) {
            wolmoFragment.onVisible()
            visible = true
        } else if ((!menuVisible || !fragment.isResumed) && visible) {
            wolmoFragment.onHide()
            visible = false
        }
    }
}