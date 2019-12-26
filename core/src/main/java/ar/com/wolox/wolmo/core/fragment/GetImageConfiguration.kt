package ar.com.wolox.wolmo.core.fragment

import android.net.Uri

enum class SavingPicturePlace { GALLERY, CACHE }

interface GetImageCallback {

    fun onPermissionDenied() {}

    fun onGalleryError() {}

    fun onGalleryResponse(data: Uri?)

    fun onCameraResponse(data: Uri?)

    fun onUserCancelled() {}

    fun onUnexpectedError()
}

data class GetImageConfiguration(
    val title: String?,
    val cameraText: String?,
    val galleryText: String?,
    val imageName: String,
    val callback: GetImageCallback,
    val cameraSavingPlace: SavingPicturePlace = SavingPicturePlace.CACHE,
    val galleryDuplicateOnCache: Boolean = true)