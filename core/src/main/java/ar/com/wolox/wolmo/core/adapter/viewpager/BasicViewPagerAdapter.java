package ar.com.wolox.wolmo.core.adapter.viewpager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Basic {@link FragmentStatePagerAdapter} to manage fragments with title.
 */
public class BasicViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> mPages;
    private ArrayList<String> mTitles;

    public BasicViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        mPages = new ArrayList<>();
        mTitles = new ArrayList<>();
    }

    @Override
    @NonNull
    public Fragment getItem(int position) {
        return mPages.get(position);
    }

    @Override
    public int getCount() {
        return mPages.size();
    }

    @Override
    @Nullable
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    /**
     * Adds a fragment to the adapter with no title. The title will be set to <b>null</b>.
     *
     * @param fragment Fragment to add
     */
    public void addItem(@NonNull Fragment fragment) {
        addItem(fragment, null);
    }

    /**
     * Adds a fragment to the adapter with the given title.
     *
     * @param fragment Fragment to add
     * @param title Title for the fragment
     */
    public void addItem(@NonNull Fragment fragment, @Nullable String title) {
        mPages.add(fragment);
        mTitles.add(title);
    }
}
