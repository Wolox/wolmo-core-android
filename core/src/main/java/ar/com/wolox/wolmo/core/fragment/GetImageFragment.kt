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
package ar.com.wolox.wolmo.core.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
import ar.com.wolox.wolmo.core.permission.PermissionListener
import ar.com.wolox.wolmo.core.util.ImageProvider
import ar.com.wolox.wolmo.core.util.WolmoFileProvider
import java.io.File
import javax.inject.Inject

/**
 * Class to help to load images from the gallery or from the camera.
 */
abstract class GetImageFragment<T : GetImagePresenter<*>> : WolmoFragment<T>(), GetImageView {

    /**
     * Text to show as the title of the dialog.
     * If it's null, the title will be empty.
     *
     * Default implementation is null. Override if needed.
     */
    open val dialogTitle: String? = null

    /**
     * Text to show on the camera button of the dialog.
     * If it's null, the button text will be "Take a picture".
     *
     * Default implementation is null. Override if needed.
     */
    open val dialogCameraText: String? = null

    /**
     * Text to show on the gallery button of the dialog.
     * If it's null, the button text will be "Upload from gallery".
     *
     * Default implementation is null. Override if needed.
     */
    open val dialogGalleryText: String? = null

    private val addPhotoBottomDialogFragment: GetImageDialogFragment
        get() = GetImageDialogFragment.newInstance(dialogTitle, dialogCameraText, dialogGalleryText)

    @Inject
    lateinit var imageProvider: ImageProvider

    override fun setListeners() {
        addPhotoBottomDialogFragment.callback = object : GetImageDialogFragment.AddPhotoCallback {

            override fun onCameraRequested() = presenter.onCameraRequested()

            override fun onGalleryRequested() = presenter.onGalleryRequested()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                INTENT_CODE_IMAGE_GALLERY -> presenter.onGalleryResponse(data?.data)
                INTENT_CODE_IMAGE_CAMERA -> presenter.onCameraResponse(data?.data)
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            when (requestCode) {
                INTENT_CODE_IMAGE_GALLERY, INTENT_CODE_IMAGE_CAMERA -> presenter.onUserCancelled()
            }
        }
    }

    override fun askCameraPermissions(onGranted: () -> Unit) {
        permissionManager.requestPermission(this, object : PermissionListener() {
            override fun onPermissionsGranted() = onGranted()

            override fun onPermissionsDenied(deniedPermissions: Array<String>) {
                presenter.onPermissionDenied()
            }
        }, *CAMERA_PERMISSIONS)
    }

    override fun askGalleryPermissions(onGranted: () -> Unit) {
        permissionManager.requestPermission(this, object : PermissionListener() {
            override fun onPermissionsGranted() = onGranted()

            override fun onPermissionsDenied(deniedPermissions: Array<String>) {
                presenter.onPermissionDenied()
            }
        }, *GALLERY_PERMISSIONS)
    }

    override fun openCamera(filename: String) {
        imageProvider.getImageFromCamera(this@GetImageFragment, INTENT_CODE_IMAGE_CAMERA, filename)
    }

    override fun openGallery() {
        if (!imageProvider.getImageFromGallery(this@GetImageFragment, INTENT_CODE_IMAGE_GALLERY)) {
            presenter.onGalleryError()
        }
    }

    companion object {
        private val CAMERA_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        private val GALLERY_PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        private const val INTENT_CODE_IMAGE_GALLERY = 9000
        private const val INTENT_CODE_IMAGE_CAMERA = 9001
    }
}