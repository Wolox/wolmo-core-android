package ar.com.wolox.wolmo.core.util;

import android.content.Context;

import ar.com.wolox.wolmo.core.WoloxApplication;

public class ContextUtils {
    public static Context getAppContext() {
        return WoloxApplication.getInstance().getApplicationContext();
    }
}
