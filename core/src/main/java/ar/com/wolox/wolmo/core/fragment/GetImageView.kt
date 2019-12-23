package ar.com.wolox.wolmo.core.fragment

interface GetImageView {

    fun askCameraPermissions(onGranted: () -> Unit)

    fun askGalleryPermissions(onGranted: () -> Unit)

    fun openCamera(filename: String)

    fun openGallery()

    fun showCameraError()

    fun showGalleryError()

    fun showUnexpectedError()
}
