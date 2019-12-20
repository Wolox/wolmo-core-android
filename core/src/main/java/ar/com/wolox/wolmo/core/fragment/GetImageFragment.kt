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
import ar.com.wolox.wolmo.core.presenter.BasePresenter
import ar.com.wolox.wolmo.core.util.ImageProvider
import ar.com.wolox.wolmo.core.util.WolmoFileProvider
import java.io.File
import javax.inject.Inject

/**
 * Class to help to load images from the gallery or from the camera.
 *
 * @param <T> Presenter for this fragment
</T> */
abstract class GetImageFragment<T : GetImagePresenter<*>> : WolmoFragment<T>() {

    private val addPhotoBottomDialogFragment = GetImageDialogFragment.newInstance()

    private var mPictureTakenFile: File? = null
    private var mImageCallback: OnImageReturnCallback? = null

    @JvmField
    @Inject
    var mImageProvider: ImageProvider? = null

    @JvmField
    @Inject
    var mWolmoFileProvider: WolmoFileProvider? = null

    override fun setListeners() {
        addPhotoBottomDialogFragment.callback = object : GetImageDialogFragment.AddPhotoCallback {

            override fun onCameraRequested() = presenter.onCameraRequested()

            override fun onGalleryRequested() = presenter.onGalleryRequested()
        }
    }

    /**
     * Error Types returned on [OnImageReturnCallback.error]
     *  *
     * [Error.USER_CANCELED]: Image selection canceled by the user.
     * [Error.ERROR_DATA]: Image URI was not returned.
     * [Error.ERROR_UNKNOWN]: Unknown error.
     * [Error.PERMISSION_DENIED]: Gallery/Camera permission denied.
     *
     */
    enum class Error {
        USER_CANCELED,  // Image selection canceled by the user
        ERROR_DATA,  // Image URI was no returned
        ERROR_UNKNOWN,  // Unknown error
        PERMISSION_DENIED // Gallery/Camera Permission denied
    }

    /**
     * Callback for image selection
     */
    interface OnImageReturnCallback {
        /**
         * Method for when the image retrieval was a success, exposing the [Uri] of the image.
         *
         * @param file retrieved image file
         * Returning the Uri brought us some trouble. Anyway, it can be easily retrieved
         * calling [Uri.fromFile]
         */
        fun success(file: File)

        /**
         * Method for when the image retrieval was a failure, exposing the corresponding
         * [Error] instance.
         *
         * @param error describing the failure reason
         */
        fun error(error: Error)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                INTENT_CODE_IMAGE_GALLERY -> if (data != null && data.data != null) {
                    val pathUri = mWolmoFileProvider!!.getRealPathFromUri(data.data!!)
                    mImageCallback!!.success(File(pathUri))
                } else {
                    notifyError(Error.ERROR_DATA)
                }
                INTENT_CODE_IMAGE_CAMERA -> {
                    mImageProvider!!.addPictureToDeviceGallery(Uri.fromFile(mPictureTakenFile))
                    mImageCallback!!.success(mPictureTakenFile!!)
                }
                else -> notifyError(Error.ERROR_UNKNOWN)
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            when (requestCode) {
                INTENT_CODE_IMAGE_GALLERY, INTENT_CODE_IMAGE_CAMERA -> notifyError(Error.USER_CANCELED)
                else -> notifyError(Error.ERROR_UNKNOWN)
            }
        }
        clearCallbacks()
    }

    private fun clearCallbacks() {
        mImageCallback = null
    }

    private fun notifyError(error: Error) {
        if (mImageCallback != null) {
            mImageCallback!!.error(error)
        }
    }

    /**
     * Resource ID to show as a toast if there is an error obtaining the image from the gallery.
     *
     * @return Error string resource id
     */
    @StringRes
    protected abstract fun galleryErrorResId(): Int

    /**
     * Resource ID to show as a toast if there is an error obtaining the image from the camera.
     *
     * @return Error string resource id
     */
    @StringRes
    protected abstract fun cameraErrorResId(): Int

    /**
     * Filename of the image taken by the camera.
     *
     * @return Image filename
     */
    protected abstract fun pictureTakenFilename(): String

    /**
     * Start a request for an image from Gallery.
     *
     * @param onImageReturnCallback callback for request result
     */
    fun selectImageFromGallery(
        onImageReturnCallback: OnImageReturnCallback) {
        permissionManager.requestPermission(this, object : PermissionListener() {
            override fun onPermissionsGranted() {
                mImageCallback = onImageReturnCallback
                mImageProvider!!.getImageFromGallery(this@GetImageFragment, INTENT_CODE_IMAGE_GALLERY,
                    galleryErrorResId())
            }

            override fun onPermissionsDenied(deniedPermissions: Array<String>) {
                onImageReturnCallback.error(Error.PERMISSION_DENIED)
            }
        }, *GALLERY_PERMISSIONS)
    }

    /**
     * Start a request for an image from Camera.
     *
     * @param onImageReturnCallback callback for request result
     */
    fun takePicture(onImageReturnCallback: OnImageReturnCallback) {
        permissionManager.requestPermission(this, object : PermissionListener() {
            override fun onPermissionsGranted() {
                mImageCallback = onImageReturnCallback
                mPictureTakenFile = mImageProvider
                    .getImageFromCamera(this@GetImageFragment, INTENT_CODE_IMAGE_CAMERA,
                        pictureTakenFilename(), ImageProvider.PNG, cameraErrorResId())
            }

            override fun onPermissionsDenied(deniedPermissions: Array<String>) {
                onImageReturnCallback.error(Error.PERMISSION_DENIED)
            }
        }, *CAMERA_PERMISSIONS)
    }

    companion object {
        private val CAMERA_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        private val GALLERY_PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        private const val INTENT_CODE_IMAGE_GALLERY = 9000
        private const val INTENT_CODE_IMAGE_CAMERA = 9001
    }
}