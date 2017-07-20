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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

import ar.com.wolox.wolmo.core.di.scopes.ApplicationScope;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Helper class to handle Android's runtime permissions
 */
@ApplicationScope
public class PermissionManager {

    private int mRequestCount = 1;
    private SparseArray<PermissionListener> mRequestListeners;
    private Context mContext;

    @Inject
    public PermissionManager(Context context) {
        mContext = context;
        mRequestListeners = new SparseArray<>();
    }

    /**
     * Request one or more Android's runtime permissions using a {@link Fragment} to perform the
     * request.
     *
     * @param fragment    An instance of the {@link Fragment} that requires the permissions.
     * @param listener    A {@link PermissionListener} to receive the response of the request. Can
     *                    be null if not interested in the response.
     * @param permissions One or more {@link String} objects with the permissions to be requested.
     * @return returns true if every requested permission was already granted, false otherwise.
     */
    public boolean requestPermission(@NonNull Fragment fragment,
                                     @Nullable PermissionListener listener,
                                     @NonNull String... permissions) {
        String[] ungrantedPermissions = filterUngranted(fragment.getActivity(), permissions);
        if (ungrantedPermissions.length > 0) {
            fragment.requestPermissions(ungrantedPermissions, mRequestCount);
            mRequestListeners.put(mRequestCount++, listener);
            return false;
        }
        if (listener != null) listener.onPermissionsGranted();
        return true;
    }

    /**
     * Request one or more Android's runtime permissions using an {@link Activity} to perform the
     * request.
     *
     * @param activity    An instance of the {@link Activity} that requires the permissions.
     * @param listener    A {@link PermissionListener} to receive the response of the request. Can
     *                    be null if not interested in the response.
     * @param permissions One or more {@link String} objects with the permissions to be requested.
     * @return returns true if every requested permission was already granted, false otherwise.
     */
    public boolean requestPermission(@NonNull Activity activity,
                                     @Nullable PermissionListener listener,
                                     @NonNull String... permissions) {
        String[] ungrantedPermissions = filterUngranted(activity, permissions);
        if (ungrantedPermissions.length > 0) {
            ActivityCompat.requestPermissions(activity, ungrantedPermissions, mRequestCount);
            mRequestListeners.put(mRequestCount++, listener);
            return false;
        }
        if (listener != null) listener.onPermissionsGranted();
        return true;
    }

    /**
     * Callback that must be called from Activities and Fragments that will make use of
     * {@link PermissionManager}.
     * <p>
     * If using an {@link Activity}, the method
     * {@link Activity#onRequestPermissionsResult(int, String[], int[])} should call THIS method
     * to delegate its behaviour.
     * <p>
     * If using a {@link Fragment}, the method
     * {@link Fragment#onRequestPermissionsResult(int, String[], int[])} should call THIS method
     * to delegate its behaviour.
     *
     * @param requestCode  The request code passed when requesting the permissions.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions which is either
     *                     {@link android.content.pm.PackageManager#PERMISSION_GRANTED} or
     *                     {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     */
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull final int[] grantResults) {
        final PermissionListener listener = mRequestListeners.get(requestCode);
        if (listener != null) {
            mRequestListeners.remove(requestCode);
            if (allGranted(grantResults)) {
                // Workaround to Android bug: https://goo.gl/OwseuO
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onPermissionsGranted();
                    }
                });
            } else {
                listener.onPermissionsDenied(filterUngranted(mContext, permissions));
            }
        }
    }

    /**
     * Filters a list of permissions and returns only the ones which have not been granted.
     *
     * @param context     An instance of {@link Context}. Cannot be null.
     * @param permissions An array of {@link String} with the permissions that will be filtered.
     * @return An array of {@link String} representing permissions that are not currently granted.
     */
    @NonNull
    private String[] filterUngranted(@NonNull Context context, @NonNull String... permissions) {
        ArrayList<String> ungranted = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ungranted.add(permission);
            }
        }
        return ungranted.toArray(new String[ungranted.size()]);
    }

    /**
     * Checks if every required permission was granted based on the results obtained in
     * {@link PermissionManager#onRequestPermissionsResult(int, String[], int[])}.
     *
     * @param results An array of int provided by
     *                {@link PermissionManager#onRequestPermissionsResult(int, String[], int[])}
     * @return returns true if every permission was granted, false otherwise.
     */
    private boolean allGranted(int[] results) {
        if (results.length < 1) return false;
        boolean granted = true;
        for (int result : results) {
            granted = granted && result == PackageManager.PERMISSION_GRANTED;
        }
        return granted;
    }
}