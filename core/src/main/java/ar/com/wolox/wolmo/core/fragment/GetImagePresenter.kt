package ar.com.wolox.wolmo.core.fragment

import ar.com.wolox.wolmo.core.presenter.CoroutineBasePresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


abstract class UploadImagePresenter<T : UploadImageView> : CoroutineBasePresenter<T>() {

    @Inject
    lateinit var filesHelper: FilesHelper

    private suspend fun copyImageToCache(file: File) = filesHelper.copyImageToCache(file)?.also {
        onPictureAvailable(it)
    }

    fun onCameraRequested() = view.openCamera()

    fun onGalleryRequested() = view.openGallery()

    fun onCameraSuccess(file: File) {
        launch(Dispatchers.Main) {
            copyImageToCache(file)?.let {
                file.delete()
                view.closeDialog()
                view.notifyImageDeltedInGallery(file)
            } ?: run {
                view.showCameraError()
            }
        }
    }

    fun onCameraError() = view.showCameraError()

    fun onGallerySuccess(file: File) {
        launch(Dispatchers.Main) {
            copyImageToCache(file)?.let {
                view.closeDialog()
            } ?: run {
                view.showGalleryError()
            }
        }
    }

    fun onGalleryError() = view.showGalleryError()

    fun onUserCancelled() {}

    fun onPermissionDenied() = view.showPermissionsError()

    abstract fun onPictureAvailable(pictureUrl: String)
}