package ar.com.wolox.wolmo.core.permissions

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

class PermissionManager(
    private val componentActivity: ComponentActivity,
    private val permissionsRequested: Map<String, PermissionResponse>
) {
    fun request() {
        val permissionsToRequest = mutableMapOf<String, PermissionResponse>()
        permissionsRequested.forEach { (permissionName, permissionResponseHandler) ->
            when {
                ContextCompat.checkSelfPermission(
                    componentActivity,
                    permissionName
                ) == PackageManager.PERMISSION_GRANTED -> {
                    permissionResponseHandler.onAlreadyGranted()
                }
                shouldShowRequestPermissionRationale(componentActivity, permissionName) -> {
                    /*
                        In an educational UI, explain to the user why your app requires this
                        permission for a specific feature to behave as expected, and what
                        features are disabled if it's declined. In this UI, include a
                        "cancel" or "no thanks" button that lets the user continue
                        using your app without granting the permission.

                        A 'rationale' actions happens when the user previously has denied the request.
                        In this implementation, it's implicit that we don't trigger directly a UI
                        (since this is a forEach, it may trigger multiple UI's on screen),
                        but we should trigger a signal after this loop ends to properly handle
                        rationale requests.
                     */
                    permissionResponseHandler.onRationale()
                }
                else -> {
                    // Save the actual permissions requests to be performed.
                    permissionsToRequest[permissionName] = permissionResponseHandler
                }
            }
        }
        if ( permissionsToRequest.size > 1 ){
            RequestPermissionLauncher.requestMany(componentActivity, permissionsToRequest)
        } else if (permissionsToRequest.size == 1) {
            val permissionName = permissionsToRequest.keys.first()
            permissionsToRequest[permissionName]?.let { handler ->
                RequestPermissionLauncher.request(componentActivity, handler, permissionName)
            }
        }
    }
}


/**
 * Method Object that handles the permission request launcher contract for an activity.
 */
object RequestPermissionLauncher {

    /**
     * Register the permissions callback, which handles the user's response
     * to the system permissions dialog.
     * Save the return value, an instance of ActivityResultLauncher.
     * You can use either a val, as shown in this snippet,
     * or a lateinit var in your onAttach() or onCreate() method.
     * @param componentActivity activity where the permission dialog will be shown.
     * @param permissionResponse handler for the onGranted and onDenied situations.
     * @param permissionName name of the requested permission.
     */
    fun request(
        componentActivity: ComponentActivity,
        permissionResponse: PermissionResponse,
        permissionName: String
    ) {
        val permissionRequestLauncher = componentActivity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                permissionResponse.onGranted()
            } else {
                // Explain to the user that the feature is unavailable because the
                // feature requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                permissionResponse.onDenied()
            }
        }
        permissionRequestLauncher.launch(permissionName)
    }

    fun requestMany(
        componentActivity: ComponentActivity,
        permissionResponse: Map<String, PermissionResponse>
    ) {
        val permissionsRequestLauncher = componentActivity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionRequestResult ->
            permissionRequestResult.mapValues { (permissionName, isGranted) ->
                if(isGranted) {
                    permissionResponse[permissionName]?.onGranted()
                }
                else {
                    permissionResponse[permissionName]?.onDenied()
                }
            }
        }
        permissionsRequestLauncher.launch(permissionResponse.keys.toTypedArray())
    }
}


interface PermissionResponse {
    fun onGranted()
    fun onDenied()
    fun onRationale()
    fun onAlreadyGranted() {}
}
