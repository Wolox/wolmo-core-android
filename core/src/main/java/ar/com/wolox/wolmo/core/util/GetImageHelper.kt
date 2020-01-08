package ar.com.wolox.wolmo.core.util

import android.Manifest
import androidx.fragment.app.Fragment
import ar.com.wolox.wolmo.core.permission.PermissionListener
import ar.com.wolox.wolmo.core.permission.PermissionManager
import javax.inject.Inject

/**
 * A helper class to open gallery and camera to get an image.
 */
class GetImageHelper @Inject constructor(
    private val permissionManager: PermissionManager,
    private val imageProvider: ImageProvider
) {

    /**
     * Open the gallery from the given [fragment]. Override [Fragment.onActivityResult] and check
     * given [code] to get result. [onPermissionDenied] will be invoked if camera gallery
     * permissions have been denied and [onGalleryNotFound] will be invoked if there's no
     * gallery application on the device.
     */
    fun openGallery(
        fragment: Fragment,
        code: Int,
        onPermissionDenied: () -> Unit = {},
        onGalleryNotFound: () -> Unit = {}
    ) {
        permissionManager.requestPermission(fragment.requireActivity(), object : PermissionListener() {
            override fun onPermissionsGranted() {
                if (!imageProvider.getImageFromGallery(fragment, code)) {
                    onGalleryNotFound()
                }
            }

            override fun onPermissionsDenied(deniedPermissions: Array<String>) {
                onPermissionDenied()
            }
        }, *GALLERY_PERMISSIONS)
    }

    /**
     * Open the camera from the given [fragment] and save the picture into [destinationFilename].
     * Override [Fragment.onActivityResult] and check given [code] to get result.
     * [onPermissionDenied] will be invoked if camera gallery permissions have been
     * denied and [onCameraNotFound] will be invoked if there's no camera application on the device.
     */
    fun openCamera(
        fragment: Fragment,
        code: Int,
        destinationFilename: String,
        onPermissionDenied: () -> Unit = {},
        onCameraNotFound: () -> Unit = {}
    ) {

        permissionManager.requestPermission(fragment.requireActivity(), object : PermissionListener() {
            override fun onPermissionsGranted() {
                if (!imageProvider.getImageFromCamera(fragment, code, destinationFilename)) {
                    onCameraNotFound()
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