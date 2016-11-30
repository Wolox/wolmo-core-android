package ar.com.wolox.wolmo.core;

import android.app.Application;

import ar.com.wolox.wolmo.core.service.WoloxRetrofitServices;

public abstract class WoloxApplication extends Application {

    private static WoloxApplication sApplication;
    private static WoloxRetrofitServices sRetrofitServices;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        sRetrofitServices = getRetrofitServices();
        sRetrofitServices.init();
        onInit();
    }

    // Here you init the other libs
    public abstract void onInit();

    public abstract WoloxRetrofitServices getRetrofitServices();

    public static WoloxApplication getInstance() {
        return sApplication;
    }
}
