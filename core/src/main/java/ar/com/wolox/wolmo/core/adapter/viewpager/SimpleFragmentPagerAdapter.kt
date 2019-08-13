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
package ar.com.wolox.wolmo.core.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import javax.inject.Inject

/**
 * Basic implementation of [FragmentPagerAdapter] with support for Titles.
 *
 * To use it with dagger you need to provide the [FragmentManager] from the activity you wish
 * to use the adapter from.
 *
 * ```
 *  @Module
 *  class ActivityModule {
 *
 *      @Provides
 *      @PerActivity
 *      fun provideFragmentManager(activity: Activity): FragmentManager = activity.getFragmentManager();
 *  }
 *  ```
 */
class SimpleFragmentPagerAdapter @Inject constructor(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    private val fragmentsAndTitles: MutableList<Pair<Fragment, String>> = mutableListOf()

    /**
     * Adds a variable length argument with instances of [fragmentsAndTitles] represented by a [Pair] containing a
     * [Fragment] and corresponding [String] with the title for that [Fragment].
     */
    @SafeVarargs
    fun addFragments(vararg fragmentsAndTitles: Pair<Fragment, String>) {
        this.fragmentsAndTitles.addAll(fragmentsAndTitles)
        notifyDataSetChanged()
    }

    /** Adds a [fragment] instance with a corresponding [title] to the adapter. */
    fun addFragment(fragment: Fragment, title: String) = addFragments(Pair(fragment, title))

    /** Refer to [FragmentPagerAdapter] documentation. */
    override fun getItem(position: Int) = fragmentsAndTitles[position].first

    /** Refer to [FragmentPagerAdapter] documentation. */
    override fun getCount() = fragmentsAndTitles.size

    /** Refer to [FragmentPagerAdapter] documentation. */
    override fun getPageTitle(position: Int) = fragmentsAndTitles[position].second
}
