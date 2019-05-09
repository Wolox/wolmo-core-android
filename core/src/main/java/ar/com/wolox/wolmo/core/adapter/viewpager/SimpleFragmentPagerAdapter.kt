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

import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*
import javax.inject.Inject

/**
 * Basic implementation of [FragmentPagerAdapter] with support for Titles.
 *
 * To use it with dagger you need to provide the [FragmentManager] from the activity you wish
 * to use the adapter from.
 *
 * <pre>`@Module
 * public class ActivityModule {
 *
 * @Provides
 * @PerActivity
 * FragmentManager provideFragmentManager(Activity activity) {
 * return activity.getFragmentManager();
 * }
 * }
`</pre> *
 */
class SimpleFragmentPagerAdapter
/**
 * Constructor, requires an instance of a [FragmentManager].
 *
 * @param fm An instance of [FragmentManager]. Cannot be null.
 */
@Inject
constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private val mFragmentsAndTitles: ArrayList<Pair<Fragment, String>> = ArrayList()

    /**
     * Adds a variable length argument with instances of [Pair] containing a [Fragment]
     * and corresponding [String] with the title for that [Fragment].
     *
     * @param pairs A variable length of pairs of fragments and titles. Can be empty, but each
     * element cannot be null.
     */
    @SafeVarargs
    fun addFragments(vararg pairs: Pair<Fragment, String>) {
        Collections.addAll(mFragmentsAndTitles, *pairs)
        notifyDataSetChanged()
    }

    /**
     * Adds a [Fragment] instance with a corresponding [String] title to the adapter
     *
     * @param fragment an instance of [Fragment] that will be displayed in the adapter.
     * @param title a title corresponding to the [Fragment] that will be added
     */
    fun addFragment(fragment: Fragment, title: String) {
        mFragmentsAndTitles.add(Pair(fragment, title))
        notifyDataSetChanged()
    }

    /**
     * Refer to [FragmentPagerAdapter] documentation.
     */
    override fun getItem(position: Int): Fragment? {
        return mFragmentsAndTitles[position].first
    }

    /**
     * Refer to [FragmentPagerAdapter] documentation.
     */
    override fun getCount(): Int {
        return mFragmentsAndTitles.size
    }

    /**
     * Refer to [FragmentPagerAdapter] documentation.
     */
    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentsAndTitles[position].second
    }
}
