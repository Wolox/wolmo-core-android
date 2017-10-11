package ar.com.wolox.wolmo.core.di.modules;

import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;

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
    ArrayList<Pair<Fragment, String>> provideFragmentsAndTitles() {
        return new ArrayList<>();
    }
}
