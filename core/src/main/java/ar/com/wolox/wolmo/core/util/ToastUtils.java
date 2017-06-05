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
package ar.com.wolox.wolmo.core.util;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * An utility class to work with Android's {@link Toast} messages
 */
public class ToastUtils {

    /**
     * Displays a text message from a resource ID inside a {@link Toast}, briefly
     *
     * @param resId A resource ID from a {@link String} with the message to be displayed
     */
    public static void show(@StringRes int resId) {
        Toast.makeText(ContextUtils.getAppContext(), resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a given {@link String} inside a {@link Toast}, briefly
     *
     * @param message An {@link String} with the message to be displayed
     */
    public static void show(@NonNull String message) {
        Toast.makeText(ContextUtils.getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a text message from a resource ID inside a toast, during a longer than usual
     * interval
     *
     * @param resId A resource ID from a {@link String} with the message to be displayed
     */
    public static void showLong(@StringRes int resId) {
        Toast.makeText(ContextUtils.getAppContext(), resId, Toast.LENGTH_LONG).show();
    }

    /**
     * Displays a given {@link String} inside a {@link Toast}, during a longer than usual
     * interval
     *
     * @param message An {@link String} with the message to be displayed
     */
    public static void showLong(@NonNull String message) {
        Toast.makeText(ContextUtils.getAppContext(), message, Toast.LENGTH_LONG).show();
    }
}
