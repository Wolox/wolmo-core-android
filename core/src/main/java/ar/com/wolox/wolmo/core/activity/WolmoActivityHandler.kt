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
import ar.com.wolox.wolmo.core.R
import ar.com.wolox.wolmo.core.util.ToastFactory
import javax.inject.Inject

class WolmoActivityHandler @Inject constructor(
        private val mToastFactory: ToastFactory
) {
    private lateinit var wolmoActivity: WolmoActivity

    /**
     * Handles the custom lifecycle of Wolmo's Activity. It provides a set of callbacks to structure
     * the different aspects of the Activities initialization.
     *
     * savedInstanceState a [Bundle] provided by Android's lifecycle.
     * activity the [WolmoActivity] to be managed by this handler.
     */
    protected fun onCreate(activity: WolmoActivity, savedInstanceState: Bundle?) {
        wolmoActivity = activity
        wolmoActivity.apply {
            setContentView(wolmoActivity.layout())
            if (handleArguments(wolmoActivity.intent.extras)) {
                setUi()
                init()
                populate()
                setListeners()
            } else {
                mToastFactory.show(R.string.unknown_error)
                finish()
            }
        }
    }

    protected fun onDestroy() {}
}
