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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

public class NavigationUtils {

    private static final String BLANK_PAGE = "about:blank";

    /**
     * Opens the browser with a given URL.
     *
     * @param context An instance of {@link Context}. Can't be null.
     * @param url     The URL that the browser should open. If the URL is null or empty,
     *                the browser will be opened with a blank page.
     */
    public static void openBrowser(@NonNull Context context, @Nullable String url) {
        if (TextUtils.isEmpty(url)) url = BLANK_PAGE;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    /**
     * Sends an intent to start an {@link Activity} for the provided {@link Class}
     *
     * @param context An instance of {@link Context}. Can't be null.
     * @param clazz   The {@link Class} of the {@link Activity} that will be opened. Can't be null.
     */
    public static void jumpTo(@NonNull Context context, @NonNull Class clazz) {
        context.startActivity(new Intent(context, clazz));
    }

    /**
     * Sends an intent to start an {@link Activity} for the provided {@link Class}
     *
     * @param context      An instance of {@link Context}. Can't be null.
     * @param clazz        The {@link Class} of the {@link Activity} that will be opened.
     *                     Can't be null.
     * @param intentExtras Variable number of instances of {@link IntentExtra} that will be sent
     *                     as extras to the started {@link Activity}
     */
    public static void jumpTo(@NonNull Context context,
                              @NonNull Class clazz,
                              @NonNull IntentExtra... intentExtras) {
        Intent intent = new Intent(context, clazz);
        for (IntentExtra intentExtra : intentExtras) {
            intent.putExtra(intentExtra.reference, intentExtra.object);
        }
        context.startActivity(intent);
    }

    /**
     * Sends an intent to start an {@link Activity} for the provided {@link Class} but clearing
     * the current task and starting a new one.
     *
     * @param context An instance of {@link Context}. Can't be null.
     * @param clazz   The {@link Class} of the {@link Activity} that will be opened. Can't be null.
     */
    public static void jumpToClearingTask(@NonNull Context context, @NonNull Class clazz) {
        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    /**
     * Sends an intent to start an {@link Activity} for the provided {@link Class} but clearing
     * the current task and starting a new one.
     *
     * @param context      An instance of {@link Context}. Can't be null.
     * @param clazz        The {@link Class} of the {@link Activity} that will be opened.
     *                     Can't be null.
     * @param intentExtras Variable number of instances of {@link IntentExtra} that will be sent
     *                     as extras to the started {@link Activity}
     */
    public static void jumpToClearingTask(@NonNull Context context,
                                          @NonNull Class clazz,
                                          @NonNull IntentExtra... intentExtras) {
        Intent intent = new Intent(context, clazz);
        for (IntentExtra intentExtra : intentExtras) {
            intent.putExtra(intentExtra.reference, intentExtra.object);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    /**
     * Sends an intent to start an {@link Activity} for the provided {@link Class} with an
     * animation defined by {@link ActivityOptionsCompat}
     *
     * @param activity   The {@link Activity} where the intent will be sent from.
     *                   Can't be null.
     * @param clazz      The {@link Class} of the {@link Activity} that will
     *                   be opened. Can't be null.
     * @param transition An instance of {@link ActivityOptionsCompat} that defines
     *                   the animation behaviour.
     */
    public static void jumpToWithAnimation(@NonNull Activity activity,
                                           @NonNull Class clazz,
                                           @NonNull ActivityOptionsCompat transition) {
        ActivityCompat.startActivity(activity, new Intent(activity, clazz), transition.toBundle());
    }

    /**
     * Sends an intent to start an {@link Activity} for the provided {@link Class} with an
     * animation defined by {@link ActivityOptionsCompat}
     *
     * @param activity     The {@link Activity} where the intent will be sent from.
     *                     Can't be null.
     * @param clazz        The {@link Class} of the {@link Activity} that will
     *                     be opened. Can't be null.
     * @param transition   An instance of {@link ActivityOptionsCompat} that defines
     *                     the animation behaviour.
     * @param intentExtras Variable number of instances of {@link IntentExtra} that
     *                     will be sent as extras to the started {@link Activity}
     */
    public static void jumpToWithAnimation(@NonNull Activity activity,
                                           @NonNull Class clazz,
                                           @NonNull ActivityOptionsCompat transition,
                                           @NonNull IntentExtra... intentExtras) {
        Intent intent = new Intent(activity, clazz);
        for (IntentExtra intentExtra : intentExtras) {
            intent.putExtra(intentExtra.reference, intentExtra.object);
        }
        ActivityCompat.startActivity(activity, intent, transition.toBundle());
    }

    /**
     * Returns an instance of {@link ActivityOptionsCompat} constructed from an instance of the
     * {@link Activity} where the intent will be send and a number of pairs of {@link View} and
     * {@link String} representing the shared view in the transition.
     *
     * @param activity The {@link Activity} where the intent is being started.
     * @param pairs    Paris of {@link View} and {@link String} with the shared elements.
     * @return A non null instance of @{@link ActivityOptionsCompat}
     */
    @SafeVarargs
    @NonNull
    public static ActivityOptionsCompat buildActivityOptions(
            @NonNull Activity activity, @NonNull Pair<View, String>... pairs) {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs);
    }

    public static class Builder {

        private Activity activity;
        private Class clazz;
        private ArrayList<Pair<View, String>> sharedElements;
        private ArrayList<IntentExtra> mIntentExtras;

        public Builder(@NonNull Activity activity) {
            this.activity = activity;
            sharedElements = new ArrayList<>();
            mIntentExtras = new ArrayList<>();
        }

        public Builder setClass(@NonNull Class clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder addSharedElement(@NonNull View sharedView, @NonNull String sharedString) {
            sharedElements.add(new Pair<View, String>(sharedView, sharedString));
            return this;
        }

        public Builder addIntentObjects(@NonNull IntentExtra... intentExtras) {
            for (IntentExtra intentExtra : intentExtras) {
                addIntentObject(intentExtra);
            }
            return this;
        }

        public Builder addIntentObject(@NonNull IntentExtra intentExtra) {
            this.mIntentExtras.add(intentExtra);
            return this;
        }

        public Builder addExtra(@NonNull String reference, @NonNull Serializable object) {
            addIntentObject(new IntentExtra(reference, object));
            return this;
        }

        public void jump() {
            if (sharedElements.isEmpty()) {
                jumpTo(activity, clazz,
                        mIntentExtras.toArray(new IntentExtra[mIntentExtras.size()]));
            } else {
                jumpToWithAnimation(activity, clazz, buildActivityOptions(activity,
                        sharedElements.toArray(new Pair[sharedElements.size()])),
                        mIntentExtras.toArray(new IntentExtra[mIntentExtras.size()]));
            }

        }
    }

    /**
     * An utility class to pair {@link Intent} extras with their corresponding references.
     */
    public static class IntentExtra {
        private String reference;
        private Serializable object;

        /**
         * Constructor that binds an {@link Intent} extra with its corresponding reference.
         *
         * @param reference A reference for an {@link Intent} extra. Can't be null.
         * @param object    An instance of an {@link Intent} extra. Can't be null.
         */
        public IntentExtra(@NonNull String reference, @NonNull Serializable object) {
            this.reference = reference;
            this.object = object;
        }
    }
}