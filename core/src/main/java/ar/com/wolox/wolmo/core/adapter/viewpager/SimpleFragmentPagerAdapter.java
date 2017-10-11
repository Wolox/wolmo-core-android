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
package ar.com.wolox.wolmo.core.adapter.viewpager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Basic implementation of {@link FragmentPagerAdapter} with support for Titles.
 *
 * To use it with dagger you need to provide the {@link FragmentManager} from the activity you wish
 * to use the adapter from.
 *
 * <pre>{@code
 *      @literal @Module
 *       public class ActivityModule {
 *
 *          @literal @Provides
 *          @literal @PerActivity
 *           FragmentManager fragmentManager() {
 *               return activity.getFragmentManager();
 *           }
 *       }
 *   }</pre>
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    @Inject
    ArrayList<Pair<Fragment, String>> mFragmentsAndTitles;

    /**
     * Constructor, requires an instance of a {@link FragmentManager}.
     *
     * @param fm    An instance of {@link FragmentManager}. Cannot be null.
     */
    @Inject
    public SimpleFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    /**
     * Adds a variable length argument with instances of {@link Pair} containing a {@link Fragment}
     * and corresponding {@link String} with the title for that {@link Fragment}.
     *
     * @param pairs A variable length of pairs of fragments and titles. Can be empty, but each
     *              element cannot be null.
     */
    @SafeVarargs
    public final void addFragments(@NonNull Pair<Fragment, String>... pairs) {
        Collections.addAll(mFragmentsAndTitles, pairs);
        notifyDataSetChanged();
    }

    /**
     * Adds a {@link Fragment} instance with a corresponding {@link String} title to the adapter
     *
     * @param fragment an instance of {@link Fragment} that will be displayed in the adapter.
     * @param title    a title corresponding to the {@link Fragment} that will be added
     */
    public void addFragment(@NonNull Fragment fragment, @NonNull String title) {
        mFragmentsAndTitles.add(new Pair<>(fragment, title));
        notifyDataSetChanged();
    }

    /**
     * Refer to {@link FragmentPagerAdapter} documentation.
     */
    @Override
    public Fragment getItem(int position) {
        return mFragmentsAndTitles.get(position).first;
    }

    /**
     * Refer to {@link FragmentPagerAdapter} documentation.
     */
    @Override
    public int getCount() {
        return mFragmentsAndTitles.size();
    }

    /**
     * Refer to {@link FragmentPagerAdapter} documentation.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentsAndTitles.get(position).second;
    }
}
