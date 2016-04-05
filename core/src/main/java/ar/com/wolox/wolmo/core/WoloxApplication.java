package ar.com.wolox.wolmo.core;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

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
        setUpFresco();
        onInit();
    }

    // Here you init the another libs
    public abstract void onInit();

    private void setUpFresco() {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);
    }

    public abstract WoloxRetrofitServices getRetrofitServices();

    public static WoloxApplication getInstance() {
        return sApplication;
    }
}
