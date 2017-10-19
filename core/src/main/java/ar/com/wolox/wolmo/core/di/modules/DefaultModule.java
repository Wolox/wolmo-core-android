package ar.com.wolox.wolmo.core.di.modules;

import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.SparseArray;

import ar.com.wolox.wolmo.core.permission.PermissionListener;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;

import java.util.ArrayList;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

/**
 * Provides default implementations for Wolmo's dependencies and utils.
 * You can add this module to your {@link Component} if you don't need to customize any dependency.
 */
@Module
public class DefaultModule {

    @Provides
    static ArrayList<Pair<Fragment, String>> provideFragmentsAndTitles() {
        return new ArrayList<>();
    }

    @Provides
    static SparseArray<PermissionListener> providesPermissionManagerArray() {
        return new SparseArray<>();
    }

    @Provides
    static BasePresenter providesBasePresenter() {
        return new BasePresenter();
    }
}
