/*
 * Copyright (c) Wolox S.A
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ar.com.wolox.wolmo.core.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;

import ar.com.wolox.wolmo.core.util.ContextUtils;

public class PermissionManager {

    private static PermissionManager sInstance;

    private int sRequestCount = 1;
    private HashMap<Integer, PermissionListener> sRequestListeners = new HashMap<>();

    private PermissionManager() {};

    public synchronized static PermissionManager getInstance() {
        if(sInstance == null) {
            sInstance = new PermissionManager();
        }
        return sInstance;
    }

    public boolean requirePermission(Fragment fragment, PermissionListener listener, String... permissions) {
        String[] ungrantedPermissions = filterUngranted(fragment.getActivity(), permissions);
        if (ungrantedPermissions.length > 0) {
            fragment.requestPermissions(ungrantedPermissions, sRequestCount);
            sRequestListeners.put(sRequestCount++, listener);
            return false;
        }
        listener.onPermissionsGranted();
        return true;
    }

    public boolean requirePermission(Activity activity, PermissionListener listener, String... permissions) {
        String[] ungrantedPermissions = filterUngranted(activity, permissions);
        if (ungrantedPermissions.length > 0) {
            ActivityCompat.requestPermissions(activity, ungrantedPermissions, sRequestCount);
            sRequestListeners.put(sRequestCount++, listener);
            return false;
        }
        listener.onPermissionsGranted();
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, final int[] grantResults) {
        final PermissionListener listener = sRequestListeners.get(requestCode);
        if(listener != null) {
            sRequestListeners.remove(requestCode);
            if (allGranted(grantResults)) {
                // Workaround to Android bug: https://goo.gl/OwseuO
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onPermissionsGranted();
                    }
                });
            } else {
                listener.onPermissionsDenied(filterUngranted(ContextUtils.getAppContext(), permissions));
            }
        }
    }

    private String[] filterUngranted(Context context, String... permissions) {
        ArrayList<String> ungranted = new ArrayList<>();
        for(String permission : permissions) {
            if(ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ungranted.add(permission);
            }
        }
        return ungranted.toArray(new String[ungranted.size()]);
    }

    private boolean allGranted(int[] results) {
        if(results.length < 1) return false;
        boolean granted = true;
        for(int result : results) {
            granted = granted && result == PackageManager.PERMISSION_GRANTED;
        }
        return granted;
    }
}