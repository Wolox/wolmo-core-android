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
package ar.com.wolox.wolmo.core.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.util.SparseArray

import ar.com.wolox.wolmo.core.di.scopes.ApplicationScope

import java.util.ArrayList

import javax.inject.Inject

/** Helper class to handle Android's runtime permissions */
@ApplicationScope
class PermissionManager @Inject
constructor(private val context: Context, private val requestListeners: SparseArray<PermissionListener>) {

    private var requestCount = 1

    /**
     * Request one or more Android's runtime permissions using [fragment] to perform the
     * request, [listener] to receive the response of the request (can be null if not
     * interested in the response) and [permissions] ( One or more [String] objects with
     * the permissions to be requested. Returns **true** if every requested permission
     * was already granted, **false** otherwise.
     */
    fun requestPermission(fragment: Fragment, listener: PermissionListener?, vararg permissions: String): Boolean {
        val ungrantedPermissions = filterUngranted(*permissions)
        if (ungrantedPermissions.isNotEmpty()) {
            fragment.requestPermissions(ungrantedPermissions, requestCount)
            requestListeners.put(requestCount++, listener)
            return false
        }
        listener?.onPermissionsGranted()
        return true
    }

    /**
     * Request one or more Android's runtime permissions using an [activity] to perform the
     * request, [listener] to receive the response of the request (can be null if not
     * interested in the response) and [permissions] ( One or more [String] objects with
     * the permissions to be requested. Returns **true** if every requested permission
     * was already granted, **false** otherwise.
     */
    fun requestPermission(activity: Activity, listener: PermissionListener?, vararg permissions: String): Boolean {
        val ungrantedPermissions = filterUngranted(*permissions)
        if (ungrantedPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, ungrantedPermissions, requestCount)
            requestListeners.put(requestCount++, listener)
            return false
        }
        listener?.onPermissionsGranted()
        return true
    }

    /**
     * Callback that must be called from Activities and Fragments that will make use of
     * [PermissionManager]. The [requestCode] is used to get the requestListeners. If
     * listener is not null, removes [requestCode] from the requestListeners.
     * The requested [permissions] are never null. The [grantResults] for the
     * corresponding permissions is either
     * [android.content.pm.PackageManager.PERMISSION_GRANTED] or
     * [android.content.pm.PackageManager.PERMISSION_DENIED]. Never null.
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        requestListeners.get(requestCode)?.let {
            requestListeners.remove(requestCode)
            // Workaround to Android bug: https://goo.gl/OwseuO
            Handler().post {
                if (allGranted(grantResults)) {
                    it.onPermissionsGranted()
                } else {
                    it.onPermissionsDenied(filterUngranted(*permissions))
                }
            }
        }
    }

    /** Filters a list of [permissions] and returns only the ones which have not been granted. */
    private fun filterUngranted(vararg permissions: String): Array<String> {
        val ungranted = ArrayList<String>()
        permissions.filter { permission -> ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED }.forEach { ungranted.add(it) }
        return ungranted.toTypedArray()
    }

    /**
     * Checks if every required permission was granted based on the [results] obtained in
     * [PermissionManager.onRequestPermissionsResult]. Returns **true** if every permission
     * was granted, **false** otherwise.
     */
    private fun allGranted(results: IntArray): Boolean {
        if (results.isEmpty()) return false
        return results.all { it == PackageManager.PERMISSION_GRANTED }
    }
}