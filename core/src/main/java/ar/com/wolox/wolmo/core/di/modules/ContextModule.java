package ar.com.wolox.wolmo.core.di.modules;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    public static final String SHARED_PREFERENCES = "private-shared-prefs";

    @Provides
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES, Activity.MODE_PRIVATE);
    }

}
