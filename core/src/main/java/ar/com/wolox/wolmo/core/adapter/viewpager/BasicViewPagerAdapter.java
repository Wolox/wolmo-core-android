package ar.com.wolox.wolmo.core.adapter.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class BasicViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> mPages;
    private ArrayList<String> mTitles;

    public BasicViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mPages = new ArrayList<>();
        mTitles = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mPages.get(position);
    }

    @Override
    public int getCount() {
        return mPages.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    public void addItem(Fragment fragment) {
        addItem(fragment, null);
    }

    public void addItem(Fragment fragment, String title) {
        mPages.add(fragment);
        mTitles.add(title);
    }
}
