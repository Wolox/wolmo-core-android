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
package ar.com.wolox.wolmo.core.di.modules

import android.util.SparseArray
import androidx.databinding.ViewDataBinding
import ar.com.wolox.wolmo.core.fragment.WolmoFragmentHandler
import ar.com.wolox.wolmo.core.permission.PermissionListener
import ar.com.wolox.wolmo.core.presenter.BasePresenter
import ar.com.wolox.wolmo.core.util.Logger
import ar.com.wolox.wolmo.core.util.ToastFactory
import dagger.Module
import dagger.Provides

/**
 * Provides default implementations for Wolmo's dependencies and utils.
 * You can add this module to your [Component] if you don't need to customize any dependency.
 */
@Module
class DefaultModule {

    @Provides
    fun providesPermissionManagerArray() = SparseArray<PermissionListener>()

    @Provides
    fun providesDefaultBasePresenter() = BasePresenter<Any>()

    /**
     * Provides a default [WolmoFragmentHandler] with no presenter for fragments that don't
     * need it.
     */
    @Provides
    fun providesDefaultWolmoFragmentHandler(toastFactory: ToastFactory,
                                            logger: Logger,
                                            basePresenter: BasePresenter<Any>): WolmoFragmentHandler<*,*> {
        return WolmoFragmentHandler<ViewDataBinding, BasePresenter<Any>>(toastFactory, logger, basePresenter)
    }
}