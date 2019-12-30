package ar.com.wolox.wolmo.core.util

import android.Manifest
import androidx.fragment.app.Fragment
import ar.com.wolox.wolmo.core.permission.PermissionListener
import ar.com.wolox.wolmo.core.permission.PermissionManager
import javax.inject.Inject

class GetImageHelper @Inject constructor(
    private val permissionManager: PermissionManager,
    private val imageProvider: ImageProvider
) {

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun openGallery(
        fragment: Fragment,
        code: Int,
        onPermissionDenied: () -> Unit = {},
        onError: () -> Unit = {}
    ) {
        permissionManager.requestPermission(fragment.requireActivity(), object : PermissionListener() {
            override fun onPermissionsGranted() {
                if (!imageProvider.getImageFromGallery(fragment, code)) {
                    onError()
                }
            }

            override fun onPermissionsDenied(deniedPermissions: Array<String>) {
                onPermissionDenied()
            }
        }, *GALLERY_PERMISSIONS)
    }

    fun openCamera(
        fragment: Fragment,
        code: Int,
        destinationFilename: String,
        onPermissionDenied: () -> Unit = {},
        onError: () -> Unit = {}
    ) {

        permissionManager.requestPermission(fragment.requireActivity(), object : PermissionListener() {
            override fun onPermissionsGranted() {
                if (!imageProvider.getImageFromCamera(fragment, code, destinationFilename)) {
                    onError()
                }
            }

            override fun onPermissionsDenied(deniedPermissions: Array<String>) {
                onPermissionDenied()
            }
        }, *CAMERA_PERMISSIONS)
    }

    companion object {
        private val CAMERA_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        private val GALLERY_PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}