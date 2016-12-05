package ar.com.wolox.wolmo.core.util;

import android.widget.Toast;

public class ToastUtils {
    public static void showToast(int resId) {
        Toast.makeText(ContextUtils.getAppContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(String s) {
        Toast.makeText(ContextUtils.getAppContext(), s, Toast.LENGTH_SHORT).show();
    }
}
