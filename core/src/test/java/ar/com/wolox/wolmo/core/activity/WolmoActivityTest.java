package ar.com.wolox.wolmo.core.activity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItem;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ar.com.wolox.wolmo.core.R;
import ar.com.wolox.wolmo.core.fragment.IWolmoFragment;
import ar.com.wolox.wolmo.core.fragment.WolmoFragment;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerAppCompatActivity;
import static org.assertj.core.api.Java6Assertions.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ AndroidInjection.class})
public class WolmoActivityTest {

    private WolmoActivity mWolmoActivity;
    private WolmoActivityHandler mWolmoActivityHandler;

    @Before
    public void beforeTest() {
        mWolmoActivityHandler = mock(WolmoActivityHandler.class);

        mWolmoActivity = new WolmoActivity() {
            @Override
            protected int layout() {
                return 0;
            }

            @Override
            protected void init() {}
        };
        mWolmoActivity.mActivityHandler = mWolmoActivityHandler;
    }

    @Test
    public void finishActivityWhenHomeOptionIsSelected() {
        mWolmoActivity = spy(mWolmoActivity);
        MenuItem menuItem = mock(MenuItem.class);
        doReturn(android.R.id.home).when(menuItem).getItemId();
        doNothing().when(mWolmoActivity).finish();

        assertThat(mWolmoActivity.onOptionsItemSelected(menuItem)).isTrue();
        verify(mWolmoActivity, times(1)).finish();
    }

    @Test
    public void callSuperWhenExtraOptionIsSelected() {
        mWolmoActivity = spy(mWolmoActivity);
        MenuItem menuItem = mock(MenuItem.class);
        doReturn(android.R.id.addToDictionary).when(menuItem).getItemId();
        doReturn(false).when((AppCompatActivity) mWolmoActivity).onOptionsItemSelected(any(MenuItem.class));

        assertThat(mWolmoActivity.onOptionsItemSelected(menuItem)).isFalse();
        verify(mWolmoActivity, times(0)).finish();
        verify((AppCompatActivity) mWolmoActivity, times(1)).onOptionsItemSelected(menuItem);
    }

    @Test
    @SuppressWarnings("RestrictedApi")
    public void backPressedNotifiesChildFragments() {
        mWolmoActivity = spy(mWolmoActivity);

        // This fragment does not handle back pressed
        WolmoFragment wolmoFragment = mock(WolmoFragment.class);
        doReturn(true).when(wolmoFragment).isVisible();
        doReturn(false).when(wolmoFragment).onBackPressed();

        // So we continue in the list until this fragment does handle it
        WolmoFragment wolmoFragment2 = mock(WolmoFragment.class);
        doReturn(true).when(wolmoFragment2).isVisible();
        doReturn(true).when(wolmoFragment2).onBackPressed();
        // We need to handle the last one because the activity calls super and we cannot mock it

        List<WolmoFragment> fragments = Arrays.asList(wolmoFragment, wolmoFragment2);

        FragmentManager manager = mock(FragmentManager.class);
        doReturn(fragments).when(manager).getFragments();
        doReturn(manager).when(mWolmoActivity).getSupportFragmentManager();

        // Check that the activity passes the call to the fragments
        mWolmoActivity.onBackPressed();
        verify(wolmoFragment, times(1)).onBackPressed();
        verify(wolmoFragment2, times(1)).onBackPressed();
    }

    @Test
    @SuppressWarnings("RestrictedApi")
    public void backPressedNotifiesChildFragmentsAndFirstOneHandlesIt() {
        mWolmoActivity = spy(mWolmoActivity);

        // This fragment does not handle back pressed
        WolmoFragment wolmoFragment = mock(WolmoFragment.class);
        doReturn(true).when(wolmoFragment).isVisible();
        doReturn(true).when(wolmoFragment).onBackPressed();

        // So we continue in the list until this fragment does handle it
        WolmoFragment wolmoFragment2 = mock(WolmoFragment.class);
        doReturn(true).when(wolmoFragment2).isVisible();
        doReturn(true).when(wolmoFragment2).onBackPressed();
        // We need to handle the last one because the activity calls super and we cannot mock it

        List<WolmoFragment> fragments = Arrays.asList(wolmoFragment, wolmoFragment2);

        FragmentManager manager = mock(FragmentManager.class);
        doReturn(fragments).when(manager).getFragments();
        doReturn(manager).when(mWolmoActivity).getSupportFragmentManager();

        // Check that the activity passes the call to the fragments
        mWolmoActivity.onBackPressed();
        verify(wolmoFragment, times(1)).onBackPressed();
        verify(wolmoFragment2, times(0)).onBackPressed();
    }
}
