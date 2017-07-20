package ar.com.wolox.wolmo.core.di.modules;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import ar.com.wolox.wolmo.core.di.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {


    @Provides
    @ApplicationScope
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @ApplicationScope
    SharedPreferences provideSharedPreferences(String sharedPrefName, Context context) {
        return context.getSharedPreferences(sharedPrefName, Activity.MODE_PRIVATE);
    }

}
