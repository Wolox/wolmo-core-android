package ar.com.wolox.wolmo.core.di.modules;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {


    @Provides
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    SharedPreferences provideSharedPreferences(String sharedPrefName, Context context) {
        return context.getSharedPreferences(sharedPrefName, Activity.MODE_PRIVATE);
    }

}