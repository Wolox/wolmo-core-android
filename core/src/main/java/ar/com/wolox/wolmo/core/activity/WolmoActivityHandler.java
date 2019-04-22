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

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ar.com.wolox.wolmo.core.R;
import ar.com.wolox.wolmo.core.util.ToastFactory;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WolmoActivityHandler {

    private ToastFactory mToastFactory;
    private WolmoActivity mWolmoActivity;
    private Unbinder mUnbinder;

    @Inject
    WolmoActivityHandler(ToastFactory toastFactory) {
        mToastFactory = toastFactory;
    }

    /**
     * Handles the custom lifecycle of Wolmo's Activity. It provides a set of callbacks to structure
     * the different aspects of the Activities initialization.
     * Also, it provides initialization for Butterknife.
     *
     * @param savedInstanceState a {@link Bundle} provided by Android's lifecycle.
     * @param activity the {@link WolmoActivity} to be managed by this handler.
     */
    void onCreate(@NonNull WolmoActivity activity, @Nullable Bundle savedInstanceState) {
        mWolmoActivity = activity;
        mWolmoActivity.setContentView(mWolmoActivity.layout());
        mUnbinder = bindViewActivity();
        if (mWolmoActivity.handleArguments(mWolmoActivity.getIntent().getExtras())) {
            mWolmoActivity.setUi();
            mWolmoActivity.init();
            mWolmoActivity.populate();
            mWolmoActivity.setListeners();
        } else {
            mToastFactory.show(R.string.unknown_error);
            mWolmoActivity.finish();
        }
    }

    /**
     * Binds the views to WolmoActivity by calling {@link ButterKnife#bind(Activity)}
     *
     * @return Unbinder
     */
    Unbinder bindViewActivity() {
        return ButterKnife.bind(mWolmoActivity);
    }

    void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
