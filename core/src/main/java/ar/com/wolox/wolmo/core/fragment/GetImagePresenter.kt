package ar.com.wolox.wolmo.core.fragment

import android.net.Uri
import androidx.core.net.toFile
import ar.com.wolox.wolmo.core.presenter.CoroutineBasePresenter
import ar.com.wolox.wolmo.core.util.WolmoFileProvider
import ar.com.wolox.wolmo.core.extensions.unit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * A base presenter to implement when using [GetImageView]. This expects:
 * - The [imageName] with which images will be saved like "[imageName]_[System.nanoTime].png"
 * - The [cameraSavingPlace] where the camera pictures will be saved. Cache is default.
 * - A [galleryDuplicateOnCache] boolean indicating weather the image obtained from the gallery
 * is duplicated on cache or not. Default is true.
 */
abstract class GetImagePresenter<T : GetImageView> protected constructor(
    protected val imageName: String,
    protected val cameraSavingPlace: SavingPicturePlace = SavingPicturePlace.CACHE,
    protected val galleryDuplicateOnCache: Boolean = true
) : CoroutineBasePresenter<T>() {

    @Inject
    lateinit var fileProvider: WolmoFileProvider

    private val newCameraPictureFilename: String?
        get() = when (cameraSavingPlace) {
            SavingPicturePlace.CACHE -> fileProvider.getNewCachePictureFilename(imageName)
            SavingPicturePlace.GALLERY -> fileProvider.getNewImageName(imageName)
        }

    fun onCameraRequested() = view?.askCameraPermissions {
        newCameraPictureFilename?.let { view?.openCamera(it) } ?: view?.showUnexpectedError()
    }.unit

    fun onGalleryRequested() = view?.askGalleryPermissions {
        view?.openGallery()
    }.unit

    fun onGalleryResponse(data: Uri?) {
        data?.let {
            val file =
                if (galleryDuplicateOnCache) {
                    val filename = fileProvider.getNewCachePictureFilename(imageName)
                    it.toFile().copyTo(File(filename))
                } else {
                    it.toFile()
                }
            onGallerySuccess(file)
        } ?: run {
            onGalleryError()
        }
    }

    fun onCameraResponse(data: Uri?) {
        data?.let { onCameraSuccess(it.toFile()) } ?: onCameraError()
    }

    /**
     * Invoked on camera error.
     *
     * Default implementation invokes [GetImageView.showCameraError]. Override if needed.
     */
    open fun onCameraError() = view?.showCameraError()

    /**
     * Invoked on gallery error.
     *
     * Default implementation invokes [GetImageView.showGalleryError]. Override if needed.
     */
    open fun onGalleryError() = view?.showGalleryError()

    /**
     * Invoked on user cancellation.
     *
     * Default implementation does nothing. Override if needed.
     */
    open fun onUserCancelled() {}

    /**
     * Invoked on permissions denied.
     *
     * Default implementation does nothing. Override if needed.
     */
    open fun onPermissionDenied() {}

    /** Invoked on gallery success with the obtained [file]. */
    abstract fun onGallerySuccess(file: File)

    /** Invoked on camera success with the obtained [file]. */
    abstract fun onCameraSuccess(file: File)

    protected enum class SavingPicturePlace { GALLERY, CACHE }
}
