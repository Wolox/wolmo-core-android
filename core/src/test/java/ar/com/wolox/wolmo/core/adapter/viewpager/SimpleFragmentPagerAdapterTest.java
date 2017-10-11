package ar.com.wolox.wolmo.core.adapter.viewpager;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.tuple;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class SimpleFragmentPagerAdapterTest {

    private SimpleFragmentPagerAdapter mSimpleFragmentPagerAdapter;
    private FragmentManager mFragmentManager;
    private ArrayList<Pair<Fragment, String>> mFragmentsAndTitles;

    @Before
    public void beforeTest() {
        mFragmentManager = mock(FragmentManager.class);
        mFragmentsAndTitles = new ArrayList<>();

        // We need to create the object to test and inject it manually!
        mSimpleFragmentPagerAdapter = spy(new SimpleFragmentPagerAdapter(mFragmentManager));
        mSimpleFragmentPagerAdapter.mFragmentsAndTitles = mFragmentsAndTitles;
        // Do nothing when calling notifyDataSetChanged because it uses DataSetObservable
        doNothing().when(mSimpleFragmentPagerAdapter).notifyDataSetChanged();
    }

    @Test
    public void addFragmentShouldStoreAndNotifyDataSetChanged() {
        Fragment fragment = mock(Fragment.class);

        mSimpleFragmentPagerAdapter.addFragment(fragment, "Title");

        assertThat(mSimpleFragmentPagerAdapter.getCount()).isEqualTo(1);
        assertThat(mFragmentsAndTitles).extracting("first", "second").containsOnly(
            tuple(fragment, "Title"));

        verify(mSimpleFragmentPagerAdapter, times(1)).notifyDataSetChanged();
    }

    @Test
    public void addFragmentsShouldStoreAndNotifyDataSetChanged() {
        Fragment fragment1 = mock(Fragment.class);
        Fragment fragment2 = mock(Fragment.class);

        mSimpleFragmentPagerAdapter.addFragments(new Pair<>(fragment1, "Title1"),
            new Pair<>(fragment2, "Title2"));

        assertThat(mSimpleFragmentPagerAdapter.getCount()).isEqualTo(2);
        assertThat(mFragmentsAndTitles).extracting("first", "second").containsExactly(
            tuple(fragment1, "Title1"), tuple(fragment2, "Title2"));

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
