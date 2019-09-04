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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import kotlin.Pair;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SimpleFragmentPagerAdapterTest {

    private SimpleFragmentPagerAdapter mSimpleFragmentPagerAdapter;
    private FragmentManager mFragmentManager;

    @Before
    public void beforeTest() {
        mFragmentManager = mock(FragmentManager.class);

        // We need to create the object to test and inject it manually!
        mSimpleFragmentPagerAdapter = spy(new SimpleFragmentPagerAdapter(mFragmentManager));
        // Do nothing when calling notifyDataSetChanged because it uses DataSetObservable
        doNothing().when(mSimpleFragmentPagerAdapter).notifyDataSetChanged();
    }

    @Test
    public void addFragmentShouldStoreAndNotifyDataSetChanged() {
        Fragment fragment = mock(Fragment.class);

        mSimpleFragmentPagerAdapter.addFragment(fragment, "Title");

        assertThat(mSimpleFragmentPagerAdapter.getCount()).isEqualTo(1);
        assertThat(mSimpleFragmentPagerAdapter.getItem(0)).isSameAs(fragment);
        assertThat(mSimpleFragmentPagerAdapter.getPageTitle(0)).isEqualTo("Title");
        verify(mSimpleFragmentPagerAdapter, times(1)).notifyDataSetChanged();
    }

    @Test
    public void addFragmentsShouldStoreAndNotifyDataSetChanged() {
        Fragment fragment1 = mock(Fragment.class);
        Fragment fragment2 = mock(Fragment.class);

        mSimpleFragmentPagerAdapter.addFragments(new Pair<>(fragment1, "Title1"), new Pair<>(fragment2, "Title2"));

        assertThat(mSimpleFragmentPagerAdapter.getCount()).isEqualTo(2);
        assertThat(mSimpleFragmentPagerAdapter.getItem(0)).isSameAs(fragment1);
        assertThat(mSimpleFragmentPagerAdapter.getPageTitle(0)).isEqualTo("Title1");
        assertThat(mSimpleFragmentPagerAdapter.getItem(1)).isSameAs(fragment2);
        assertThat(mSimpleFragmentPagerAdapter.getPageTitle(1)).isEqualTo("Title2");
        verify(mSimpleFragmentPagerAdapter, times(1)).notifyDataSetChanged();
    }

    @Test
    public void getItemReturnsItemsInOrder() {
        Fragment fragment1 = mock(Fragment.class);
        Fragment fragment2 = mock(Fragment.class);

        mSimpleFragmentPagerAdapter.addFragments(new Pair<>(fragment1, "Title1"),
            new Pair<>(fragment2, "Title2"));
        assertThat(mSimpleFragmentPagerAdapter.getItem(0)).isEqualTo(fragment1);
        assertThat(mSimpleFragmentPagerAdapter.getItem(1)).isEqualTo(fragment2);
    }

    @Test
    public void getPageTitleReturnsTitlesInOrder() {
        Fragment fragment1 = mock(Fragment.class);
        Fragment fragment2 = mock(Fragment.class);

        mSimpleFragmentPagerAdapter.addFragments(new Pair<>(fragment1, "Title1"),
            new Pair<>(fragment2, "Title2"));
        assertThat(mSimpleFragmentPagerAdapter.getPageTitle(0)).isEqualTo("Title1");
        assertThat(mSimpleFragmentPagerAdapter.getPageTitle(1)).isEqualTo("Title2");
    }
}
